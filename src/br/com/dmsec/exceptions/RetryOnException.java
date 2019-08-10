package br.com.dmsec.exceptions;

/**
 * Encapsulates retry-on-exception operations
 */
public class RetryOnException {
    public static final int DEFAULT_RETRIES = 30;
    public static final long DEFAULT_TIME_TO_WAIT_MS = 2000;
    public static final String DEFAULT_NAME = "Name";

    private int numRetries;
    private long timeToWaitMS;
    private String name;

    
    // CONSTRUCTORS
    public RetryOnException(int _numRetries,
                            long _timeToWaitMS, String _name)
    {
        numRetries = _numRetries;
        timeToWaitMS = _timeToWaitMS;
        name = _name;
    }

    public RetryOnException()
    {
        this(DEFAULT_RETRIES, DEFAULT_TIME_TO_WAIT_MS, DEFAULT_NAME);
    }

    /**
     * shouldRetry
     * Returns true if a retry can be attempted.
     * @return  True if retries attempts remain; else false
     */
    public boolean shouldRetry()
    {
        System.out.println(name +" - numRetries: "+ numRetries);
    	return (numRetries >= 0);
    }

    /**
     * waitUntilNextTry
     * Waits for timeToWaitMS. Ignores any interrupted exception
     */
    public void waitUntilNextTry()
    {
        try {
        	System.out.println(name + " - ms: " + timeToWaitMS);
            Thread.sleep(timeToWaitMS);
        }
        catch (InterruptedException iex) { }
    }

    /**
     * exceptionOccurred
     * Call when an exception has occurred in the block. If the
     * retry limit is exceeded, throws an exception.
     * Else waits for the specified time.
     * @throws Exception
     */
    public void exceptionOccurred() throws Exception
    {
        numRetries--;
        if(!shouldRetry())
        {
            throw new Exception(name + " - Retry limit exceeded.");
        }
        waitUntilNextTry();
    }
}