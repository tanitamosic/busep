package securityproject.pki.data;

import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.asn1.x500.X500Name;

import java.security.PrivateKey;

@Getter
@Setter
public class IssuerData {

	private X500Name x500name;
	private PrivateKey privateKey;

	public IssuerData() {
	}

	public IssuerData(PrivateKey privateKey, X500Name x500name) {
		this.privateKey = privateKey;
		this.x500name = x500name;
	}

//	public IssuerData(PrivateKey privateKey, SubjectData subjectData) {
//		this.x500name = subjectData.getX500name();
//		this.privateKey = privateKey;
//
//	} // prosledi subjectdata.getX500name direkt u drugi konstruktor
}
