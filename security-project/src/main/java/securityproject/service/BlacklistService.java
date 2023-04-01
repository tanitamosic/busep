package securityproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import securityproject.model.BlacklistedCertificate;
import securityproject.model.CertificateData;
import securityproject.repository.BlacklistRepository;

import java.util.Date;

@Service
public class BlacklistService {

    @Autowired
    BlacklistRepository blacklistRepository;

    public void addCertificateToBlacklist(CertificateData data) {
        BlacklistedCertificate entry = new BlacklistedCertificate();
        entry.setEmail(data.getEmail());
        entry.setBlacklist_date(new Date());
        blacklistRepository.saveAndFlush(entry);
    }
}
