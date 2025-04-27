package uk.co.ltheobald.kafkatestbed.services;

/**
 * A simple enum to track authorisation outcomes.
 */
public enum AuthorisationOutcome {
    APPROVED("APPROVED"),
    DECLINED("DECLINED"),
    NOT_FOUND("NOT_FOUND");

    private final String outcome;

    AuthorisationOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getOutcome() {
        return outcome;
    }
}
