package productdata;

import java.io.Serializable;

/**
 * Enum class that represents a types of organizations
 */
public enum OrganizationType implements Serializable {
    PUBLIC,
    TRUST,
    PRIVATE_LIMITED_COMPANY;

    private static final long serialVersionUID = 1337000021L;
}
