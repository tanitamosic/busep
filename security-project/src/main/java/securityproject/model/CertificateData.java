package securityproject.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.asn1.x500.X500Name;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "certificates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CertificateData {
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
    @Column(name = "UID")
    private String UID;
    @Column(name = "public_key")
    private String publicKey;
    @Column(name = "issuer")
    private X500Name issuer;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "valid")
    private boolean valid;

}
