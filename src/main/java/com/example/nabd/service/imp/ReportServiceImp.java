package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.ReportDto;
import com.example.nabd.dtos.ReportMedicineDto;
import com.example.nabd.entity.Medicine;
import com.example.nabd.entity.Patient_Medicine;
import com.example.nabd.entity.Report;
import com.example.nabd.entity.Report_Medicine;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.Patient_MedicineRepo;
import com.example.nabd.repository.ReportRepo;
import com.example.nabd.repository.Report_MedicineRepo;
import com.example.nabd.service.IReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReportServiceImp implements IReportService {
    private final Patient_MedicineRepo patientMedicineRepo;
    private final ReportRepo reportRepo;
    private final Report_MedicineRepo reportMedicineRepo;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();


    public ReportServiceImp(Patient_MedicineRepo patientMedicineRepo, ReportRepo reportRepo, Report_MedicineRepo reportMedicineRepo) {
        this.patientMedicineRepo = patientMedicineRepo;
        this.reportRepo = reportRepo;
        this.reportMedicineRepo = reportMedicineRepo;
    }

    @Override
    public BasisResponse createReport() {
        List<Patient_Medicine> patient_medicines = patientMedicineRepo.findAll();
        Set<Medicine> medicines = new HashSet<>();
        List<Patient_Medicine> patientMedicinesToReport=new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        for (Patient_Medicine p : patient_medicines){
            if (p.getStartIn().getYear()==localDate.getYear()
                    &&p.getStartIn().getMonth()==localDate.getMonth()
                    &&p.getPatient().isActive()){
                medicines.add(p.getMedicine());
                patientMedicinesToReport.add(p);
            }
        }
        reportMedicineRepo.deleteAll();
        reportRepo.deleteAll();
        Report report = new Report();
        Report saved1 = reportRepo.save(report);
        List<Report_Medicine> reportMedicines = createReportMedicine(medicines,patientMedicinesToReport ,saved1);
        report.setReportMedicines(reportMedicines);
//        reportRepo.deleteAll();
//        Report saved = (Report) reportRepo.findAll();
        List<Report_Medicine> reportMedicinesSaved = reportMedicines;
        List<ReportMedicineDto> reportMedicineDtos = reportMedicinesSaved.stream().map(reportMedicine ->
                ReportMedicineDto.builder().medicineId(reportMedicine.getMedicineId()).medicine(reportMedicine.getMedicine())
                        .numberBox(reportMedicine.getNumberBox()).numberPastille(reportMedicine.getNumberPastille())
                        .totalPrice(reportMedicine.getTotalPrice()).id(reportMedicine.getId()).build()
        ).toList();
        ReportDto reportDto = ReportDto.builder().reportMedicineDto(reportMedicineDtos).id(saved1.getId()).build();
        return basisResponseMapper.createBasisResponse(reportDto);
    }

    @Override
    public BasisResponse getReport() {
        List<Report> saved = reportRepo.findAll();
        if (saved.isEmpty())  return basisResponseMapper.createBasisResponse(saved);
        System.out.println(saved.get(0).getReportMedicines());
        List<Report_Medicine> reportMedicinesSaved = saved.get(0).getReportMedicines();
        List<ReportMedicineDto> reportMedicineDtos = reportMedicinesSaved.stream().map(reportMedicine ->
                ReportMedicineDto.builder().medicineId(reportMedicine.getMedicineId()).medicine(reportMedicine.getMedicine())
                        .numberBox(reportMedicine.getNumberBox()).numberPastille(reportMedicine.getNumberPastille())
                        .totalPrice(reportMedicine.getTotalPrice()).id(reportMedicine.getId()).build()
        ).toList();
        ReportDto reportDto = ReportDto.builder().reportMedicineDto(reportMedicineDtos).id(saved.get(0).getId()).build();
        return basisResponseMapper.createBasisResponse(reportDto);
    }

    @Override
    public BasisResponse deleteReport() {
        reportMedicineRepo.deleteAll();
        reportRepo.deleteAll();
        String res = "Report deleted successfully";
        return basisResponseMapper.createBasisResponse(res);
    }
    private List<Report_Medicine> createReportMedicine(Set<Medicine> medicines,
                                                       List<Patient_Medicine> patientMedicinesToReport,
                                                       Report saved){
        List<Report_Medicine> reportMedicines = new ArrayList<>();
        for (Medicine m:
                medicines
        ) {
            int numberBox = 0;
            int numberPastille = 0;
            double totalPrice = 0;
            for (Patient_Medicine p: patientMedicinesToReport
            ) {
                if (p.getMedicine().getNameInEng().equals(m.getNameInEng())){
                    numberBox = p.getNumberBox();
                    numberPastille = p.getNumberPastille();
                }
            }
            numberBox += numberPastille / m.getNumberOfPastilleInEachBox();
            numberPastille = numberPastille % m.getNumberOfPastilleInEachBox();
            totalPrice = (numberBox*m.getPrice())+ (((double) numberPastille /m.getNumberOfPastilleInEachBox())*m.getPrice());
            Report_Medicine reportMedicine = Report_Medicine.builder().medicine(m.getNameInEng()).medicineId(m.getId())
                    .numberBox(numberBox).numberPastille(numberPastille).totalPrice(totalPrice).report(saved).build();
            reportMedicines.add(reportMedicine);
            reportMedicineRepo.save(reportMedicine);
        }
        return reportMedicines;
    }
}
