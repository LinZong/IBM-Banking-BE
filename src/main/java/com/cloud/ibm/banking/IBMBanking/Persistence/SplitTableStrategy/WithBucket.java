package com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy;

public class WithBucket<TResult>
{
    private int BucketNum;
    private TResult result;

    public WithBucket(int bucketNum, TResult result)
    {
        BucketNum = bucketNum;
        this.result = result;
    }

    public int getBucketNum()
    {
        return BucketNum;
    }

    public TResult getResult()
    {
        return result;
    }

    public void setBucketNum(int bucketNum)
    {
        BucketNum = bucketNum;
    }

    public void setResult(TResult result)
    {
        this.result = result;
    }
}
