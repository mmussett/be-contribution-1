package com.tibco.be.custom.channel.aws.sqs;

import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;

public class RetryOnException {

  private static final Logger logger = LogManagerFactory
      .getLogManager().getLogger(RetryOnException.class);

  public static final int DEFAULT_RETRIES = 30;
  public static final long DEFAULT_TIME_TO_WAIT_MS = 2000;

  private int numRetries;
  private long timeToWaitMS;

  // CONSTRUCTORS
  public RetryOnException(int _numRetries,
      long _timeToWaitMS)
  {
    numRetries = _numRetries;
    timeToWaitMS = _timeToWaitMS;
  }

  public RetryOnException()
  {
    this(DEFAULT_RETRIES, DEFAULT_TIME_TO_WAIT_MS);
  }

  /**
   * shouldRetry
   * Returns true if a retry can be attempted.
   * @return  True if retries attempts remain; else false
   */
  public boolean shouldRetry()
  {
    return (numRetries >= 0);
  }

  /**
   * waitUntilNextTry
   * Waits for timeToWaitMS. Ignores any interrupted exception
   */
  public void waitUntilNextTry()
  {
    try {
      logger.log(Level.DEBUG,"Thread sleeping for "+timeToWaitMS+" ms");
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
      logger.log(Level.FATAL,"Retry limit exceeded.");
      throw new Exception("Retry limit exceeded.");
    }
    waitUntilNextTry();
  }
}
