package securityproject.model;

import org.bouncycastle.asn1.x500.X500Name;
import securityproject.model.enums.RequestStatus;

import javax.persistence.Column;
import java.util.Date;

public class Csr {
    @Column(name="given_name")
    private String givenName;
    @Column(name = "surname")
    private String surname;
    @Column(name = "organization")
    private String organization;
    @Column(name = "organization_unit")
    private String organizationUnit;
    @Column(name = "country")
    private String country;
    @Column(name = "email")
    private String email;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "valid")
    private Boolean valid;
    @Column(name = "status")
    private RequestStatus status;
}
