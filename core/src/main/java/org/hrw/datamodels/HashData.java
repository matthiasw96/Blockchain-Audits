package org.hrw.datamodels;

public class HashData implements Datastructure {
    private final String job_id;
    private final String timestamp;
    private final String minuteHash;
    private final String hourHash;

    public HashData(HashDataBuilder builder) {
        this.job_id = builder.job_id;
        this.timestamp = builder.timestamp;
        this.minuteHash = builder.minuteHash;
        this.hourHash = builder.hourHash;
    }

    public String toString() {
        return this.timestamp + ",'" +
                this.minuteHash + "','" +
                this.hourHash + "'";
    }

    public String getJob_id() {
        return job_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMinuteHash() {
        return minuteHash;
    }

    public String getHourHash() {
        return hourHash;
    }

    @Override
    public String getAttributeNames() {
        return "job_id,\"timestamp\",MinuteHash,HourHash";
    }

    @Override
    public String getJobId() {
        return this.job_id;
    }

    public static class HashDataBuilder {
        private String job_id;
        private String timestamp;
        private String minuteHash;
        private String hourHash;

        public HashDataBuilder setJobId(String job_id) {
            this.job_id = job_id;
            return this;
        }

        public HashDataBuilder setTimestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public HashDataBuilder setMinuteHash(String MinuteHash) {
            this.minuteHash = MinuteHash;
            return this;
        }

        public HashDataBuilder setHourHash(String HourHash) {
            this.hourHash = HourHash;
            return this;
        }

        public HashData build() {
            return new HashData(this);
        }
    }
}

