package comp34120.ex2;


import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

/**
 * A pseudo leader. The members m_platformStub and m_type are declared
 * in the PlayerImpl, and feel free to use them. You may want to check
 * the implementation of the PlayerImpl. You will use m_platformStub to access
 * the platform by calling the remote method provided by it.
 * @author Xin
 */
final class Leader
		extends PlayerImpl
{
	private ArrayList<Record> records;
	private final Random m_randomizer = new Random(System.currentTimeMillis());
	private static Map<String, String> optsMap = new HashMap<String, String>();
	private Maximiser maximiser;
	private Regression linearNN;

	/**
	 * In the constructor, you need to call the constructor
	 * of PlayerImpl in the first line, so that you don't need to
	 * care about how to connect to the platform. You may want to throw
	 * the two exceptions declared in the prototype, or you may handle it
	 * by using "try {} catch {}". It's all up to you.
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	Leader()
			throws RemoteException, NotBoundException
	{
		/* The first parameter *MUST* be PlayerType.LEADER, you can change
		 * the second parameter, the name of the leader, such as "My Leader" */
		super(PlayerType.LEADER, "G20 Leader");
		m_platformStub.log(m_type, "Leader Created with following options: ");
		optsMap.entrySet().forEach(entry->{
			try {
				m_platformStub.log(m_type,entry.getKey() + " : " + entry.getValue());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		});


	}

	/**
	 * You may want to delete this method if you don't want modify
	 * the original connection checking behavior of the platform.
	 * Actually I recommend you to delete this method from your own code
	 * @throws RemoteException If implemented, the RemoteException *MUST* be
	 * thrown by this method
	 */
	@Override
	public void checkConnection()
			throws RemoteException
	{
		super.checkConnection();
		//TO DO: delete the line above and put your own code here
	}

	/**
	 * You may want to delete this method if you don't want the platform
	 * to control the exit behavior of your leader class
	 * @throws RemoteException If implemented, the RemoteException *MUST* be
	 * thrown by this method
	 */
	@Override
	public void goodbye()
			throws RemoteException
	{
		super.goodbye();
		//TO DO: delete the line above and put your own exit code here
	}

	/**
	 * You may want to delete this method if you don't want to do any
	 * initialization
	 * @param p_steps Indicates how many steps will the simulation perform
	 * @throws RemoteException If implemented, the RemoteException *MUST* be
	 * thrown by this method
	 */
	@Override
	public void startSimulation(int p_steps)
			throws Exception, RemoteException
	{
		setParameters();
		records = new ArrayList<>();
		// initialise records so we don't have to get the history each time
		for(int i = 1; i <= 100; i++) {
			Record record = m_platformStub.query(m_type, i);
			records.add(record);
		}

		//Initialize neural network on historical data
		linearNN = new NeuralNet(records);
		System.out.println("Reaction function learned from historical data: ");
		//estimate initial R(Ul)
		linearNN.estimateAB();
	}

	/**
	 * You may want to delete this method if you don't want to do any
	 * finalization
	 * @throws RemoteException If implemented, the RemoteException *MUST* be
	 * thrown by this method
	 */
	@Override
	public void endSimulation()
			throws RemoteException
	{
		// add the last record
		records.add(m_platformStub.query(m_type, records.size() + 1));
		float total = 0f;
		for(int i = 100; i < records.size(); i++) {
			Record record = records.get(i);
			total += calculateProfit(record.m_leaderPrice, record.m_followerPrice);
		}
		m_platformStub.log(m_type, "Total Profit: " + total);
	}

	/**
	 * To inform this instance to proceed to a new simulation day
	 * @param p_date The date of the new day
	 * @throws RemoteException This exception *MUST* be thrown by this method
	 */
	@Override
	public void proceedNewDay(int p_date)
			throws Exception
	{
		if(p_date > 101) {
			// Add the record from previous day
			// we need to wait until now so that the follower price is updated
			records.add(m_platformStub.query(m_type, p_date - 1));
		}

		//If using linear Regression implemented with WLS
		//Regression regression = new WLSRegression(records);
		//regression.estimateAB();
		//float bestPrice = maximiser.getBestPrice(regression, p_date);
		//m_platformStub.log(m_type, "Estimate: " + regression.getFollowerPrice(bestPrice));

		//If using linear Regression implemented with NeuralNet
		linearNN.updateRecords(records);
		linearNN.estimateAB();
		float bestPrice = maximiser.getBestPrice(linearNN, p_date);
		m_platformStub.log(m_type, "Estimate: " + linearNN.getFollowerPrice(bestPrice));


		this.m_platformStub.publishPrice(m_type, bestPrice);
	}


	private float genPrice(float p_mean, float p_diversity) {
		return (float)((double)p_mean + this.m_randomizer.nextGaussian() * (double)p_diversity);
	}

	private float demand(float leader, float follower) {
		return 2 - leader + (0.3f * follower);
	}

	private float calculateProfit(float leader, float follower) {
		return (leader - 1) * demand(leader, follower);
	}

	public static void main(String[] args) throws RemoteException, NotBoundException{
		setArgs(args);
		new Leader();
	}

	private static void setArgs(String[] args) {
		for (int i = 0; i < args.length; i++) {
			switch (args[i].charAt(0)) {
				case '-':
					if (args[i].length() < 2) throw new IllegalArgumentException("Not a valid argument: "+args[i]);
					if (args.length-1 == i) throw new IllegalArgumentException("Expected arg after: "+args[i]);
					optsMap.put(args[i].replace("-", ""), args[i+1]);
					i++;
					break;
				default:
					break;
			}
		}
	}

	private void setParameters() throws RemoteException {
		String regressionOption = optsMap.getOrDefault("R", "default");
		String maximiserOption = optsMap.getOrDefault("M", "default");
		m_platformStub.log(m_type,"REGRESSION: " + regressionOption);
		setRegression(regressionOption);
		setMaximiser(maximiserOption);
	}

	private void setMaximiser(String maximiserOption) throws RemoteException {
		m_platformStub.log(m_type, "OPTION: " + maximiserOption);
		switch(maximiserOption.toUpperCase()){
			case "CALCULUS":
				this.maximiser = new CalculusMaximiser();
				break;
			case "STANDARD":
				this.maximiser = new StandardMaximiser();
				break;
			default:
				this.maximiser = new CalculusMaximiser();
		}
		m_platformStub.log(m_type,"MAXIMISER SET TO: " + maximiser.getClass().getSimpleName());

	}

	private void setRegression(String regressionOption) {
		//TODO: IMPLEMENT MORE REGRESSION OPTIONS
	}
}
