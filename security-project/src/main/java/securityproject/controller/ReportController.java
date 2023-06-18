package securityproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import securityproject.service.ReportService;
import securityproject.util.containers.SourceReport;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "/report")
public class ReportController {

    String format = "yyyy-MM-dd";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    final static DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Autowired
    ReportService reportService;

    private static class ReportDTO {
        String reportSource;
        String logType;
        String fromDate;
        String toDate;
    }

    @PostMapping(value="/all-houses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SourceReport>> allHouses(@RequestBody ReportDTO dto) {
        try {
            String type = dto.logType;
            LocalDateTime from = LocalDateTime.from(formatter.parse(dto.fromDate));
            LocalDateTime to = LocalDateTime.from(formatter.parse(dto.toDate));
            List<SourceReport> reports = reportService.allHouses(type, from, to);
            return ResponseEntity.ok().body(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping(value="/all-devices")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SourceReport>> allDevices(@RequestBody ReportDTO dto) {
        try {
            String type = dto.logType;
            LocalDateTime from = LocalDateTime.from(formatter.parse(dto.fromDate));
            LocalDateTime to = LocalDateTime.from(formatter.parse(dto.toDate));
            List<SourceReport> reports = reportService.allDevices(type, from, to);
            return ResponseEntity.ok().body(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping(value="/user-houses")
    public ResponseEntity<List<SourceReport>> userHouses(@RequestBody ReportDTO dto) {
        try {
            String source = dto.reportSource;
            String type = dto.logType;
            LocalDateTime from = LocalDateTime.from(formatter.parse(dto.fromDate));
            LocalDateTime to = LocalDateTime.from(formatter.parse(dto.toDate));
            List<SourceReport> reports = reportService.userHousesReports(source, type, from, to);
            return ResponseEntity.ok().body(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping(value="/house-devices")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SourceReport>> houseDevices(@RequestBody ReportDTO dto) {
        try {
            Long houseId = Long.valueOf(dto.reportSource);
            String type = dto.logType;
            LocalDateTime from = LocalDateTime.from(formatter.parse(dto.fromDate));
            LocalDateTime to = LocalDateTime.from(formatter.parse(dto.toDate));
            List<SourceReport> reports = reportService.houseDevices(houseId, type, from, to);
            return ResponseEntity.ok().body(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value="/user-house/{email}")
    public ResponseEntity<List<SourceReport>> userHouse(@RequestBody ReportDTO dto, @PathVariable String email) {
        try {
            Long houseId = Long.valueOf(dto.reportSource);
            String type = dto.logType;
            LocalDateTime from = LocalDateTime.from(formatter.parse(dto.fromDate));
            LocalDateTime to = LocalDateTime.from(formatter.parse(dto.toDate));
            List<SourceReport> reports = reportService.userHouseReports(houseId, email, type, from, to);
            return ResponseEntity.ok().body(reports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
