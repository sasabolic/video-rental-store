package com.example.videorentalstore.pricing;

public class ReleasePolicyFactory {

    private static ReleasePolicy newReleasePolicy = new NewReleasePolicy();
    private static ReleasePolicy regularReleasePolicy = new RegularReleasePolicy();
    private static ReleasePolicy oldReleasePolicy = new OldReleasePolicy();

    public static ReleasePolicy of(ReleaseType type) {
        switch (type) {
            case NEW_RELEASE:
                return newReleasePolicy;
            case REGULAR_RELEASE:
                return regularReleasePolicy;
            case OLD_RELEASE:
                return oldReleasePolicy;
            default:
                throw new RuntimeException("Release policy of type '" + type + "' does not exist.");
        }
    }
}
