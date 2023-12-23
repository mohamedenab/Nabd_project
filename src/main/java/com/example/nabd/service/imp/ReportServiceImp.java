package com.example.nabd.service.imp;

import com.example.nabd.dtos.BasisResponse;
import com.example.nabd.dtos.ReportDto;
import com.example.nabd.dtos.ReportMedicineAmountDto;
import com.example.nabd.dtos.ReportMedicineDto;
import com.example.nabd.entity.*;
import com.example.nabd.exception.ResourceNotFoundException;
import com.example.nabd.mapper.BasisResponseMapper;
import com.example.nabd.repository.MedicineRepo;
import com.example.nabd.repository.Patient_MedicineRepo;
import com.example.nabd.repository.ReportRepo;
import com.example.nabd.repository.Report_MedicineRepo;
import com.example.nabd.service.IReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReportServiceImp implements IReportService {
    private final Patient_MedicineRepo patientMedicineRepo;
    private final ReportRepo reportRepo;
    private final Report_MedicineRepo reportMedicineRepo;
    private final MedicineRepo medicineRepo;
    private final BasisResponseMapper basisResponseMapper = new BasisResponseMapper();


    public ReportServiceImp(Patient_MedicineRepo patientMedicineRepo, ReportRepo reportRepo, Report_MedicineRepo reportMedicineRepo, MedicineRepo medicineRepo) {
        this.patientMedicineRepo = patientMedicineRepo;
        this.reportRepo = reportRepo;
        this.reportMedicineRepo = reportMedicineRepo;
        this.medicineRepo = medicineRepo;
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
    public BasisResponse getReport(int pageNo,int pageSize,String sortBy) {
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Report_Medicine> reportMedicines = reportMedicineRepo.findAll(pageable);
//        List<Report_Medicine> reportMedicineList = reportMedicines.getContent();
        List<Report_Medicine> reportMedicinesSaved = reportMedicines.getContent();
        List<ReportMedicineDto> reportMedicineDtos = reportMedicinesSaved.stream().map(reportMedicine ->
                ReportMedicineDto.builder().medicineId(reportMedicine.getMedicineId()).medicine(reportMedicine.getMedicine())
                        .numberBox(reportMedicine.getNumberBox()).numberPastille(reportMedicine.getNumberPastille())
                        .totalPrice(reportMedicine.getTotalPrice()).id(reportMedicine.getId()).build()
        ).toList();
        ReportDto reportDto = ReportDto.builder().reportMedicineDto(reportMedicineDtos).build();
        return basisResponseMapper.createBasisResponseForReport(reportDto,pageNo,reportMedicines);
    }

    @Override
    public BasisResponse editeMedicineAmount(Long id, ReportMedicineAmountDto reportMedicineAmountDto) {
        List<Report> report = reportRepo.findAll();
        Medicine m = medicineRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Medicine","id",id));
        Report_Medicine reportMedicine =new Report_Medicine();
        for (Report_Medicine r:
                report.get(0).getReportMedicines()
             ) {
            if (Objects.equals(r.getMedicineId(), id)){
                int numberBox = r.getNumberBox()+reportMedicineAmountDto.getNumberBox();
                int numberPastille = r.getNumberPastille()+reportMedicineAmountDto.getNumberPastille();
                double totalPrice = 0;
                numberBox += numberPastille / m.getNumberOfPastilleInEachBox();
                numberPastille = numberPastille % m.getNumberOfPastilleInEachBox();
                totalPrice = (numberBox*m.getPrice())+ (((double) numberPastille /m.getNumberOfPastilleInEachBox())*m.getPrice());
                r.setNumberBox(numberBox);
                r.setNumberPastille(numberPastille);
                r.setTotalPrice(totalPrice);
                reportMedicine = r;
                reportRepo.save(report.get(0));
                break;
            }
        }
        ReportMedicineDto reportMedicineDto = ReportMedicineDto.builder().numberPastille(reportMedicine.getNumberPastille())
                .medicineId(reportMedicine.getMedicineId()).medicine(m.getNameInEng()).numberBox(reportMedicineAmountDto.getNumberBox())
                .totalPrice(reportMedicine.getTotalPrice()).id(reportMedicine.getId()).build();
        return basisResponseMapper.createBasisResponse(reportMedicineDto);
    }

    @Override
    public BasisResponse deleteMedicine(Long id) {
        List<Report> report = reportRepo.findAll();
        report.get(0).getReportMedicines().removeIf(r -> Objects.equals(r.getMedicineId(), id));
        reportRepo.save(report.get(0));
        return basisResponseMapper.createBasisResponse("Medicine Deleted successfully");
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
