package com.example.nabd.service.imp;

import com.example.nabd.dtos.*;
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
                    &&p.getPatient().isActive()&& p.isActive()){
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
    public BasisResponse getMedicine(String filter) {
        List<Report_Medicine> reportMedicines = reportMedicineRepo.findAll();
        List<Report_Medicine> medicineFilterList = reportMedicines.stream()
                .filter(medicine -> medicine.getMedicine().contains(filter)).toList();
        List<ReportMedicineDto> reportMedicineDtos = medicineFilterList.stream().map(reportMedicine ->
                ReportMedicineDto.builder().medicineId(reportMedicine.getMedicineId()).medicine(reportMedicine.getMedicine())
                        .numberBox(reportMedicine.getNumberBox()).numberPastille(reportMedicine.getNumberPastille())
                        .totalPrice(reportMedicine.getTotalPrice()).id(reportMedicine.getId()).build()
        ).toList();
        return basisResponseMapper.createBasisResponse(reportMedicineDtos);
    }

    @Override
    public BasisResponse editeMedicine(Long id, Long newId) {
        Medicine newMedicine = medicineRepo.findById(newId).orElseThrow(()-> new ResourceNotFoundException("Medicine","id",newId));
        Medicine oldMedicine = medicineRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Medicine","id",id));
        Report_Medicine reportMedicineOld = reportMedicineRepo.findByMedicineId(id);
        if (reportMedicineOld!=null){
            double totalPrice = (reportMedicineOld.getNumberBox()*newMedicine.getPrice())+
                    (((double) reportMedicineOld.getNumberPastille()/oldMedicine.getNumberOfPastilleInEachBox()*newMedicine.getPrice()));
            Report_Medicine reportMedicine =new Report_Medicine();
            reportMedicineOld.setMedicine(newMedicine.getNameInEng());
            reportMedicineOld.setMedicineId(newMedicine.getId());
            reportMedicineOld.setTotalPrice(totalPrice);
            reportMedicine = reportMedicineRepo.save(reportMedicineOld);
            ReportMedicineDto reportMedicineDto = ReportMedicineDto.builder().numberPastille(reportMedicine.getNumberPastille())
                    .medicineId(reportMedicine.getMedicineId()).medicine(newMedicine.getNameInEng()).numberBox(reportMedicine.getNumberBox())
                    .totalPrice(reportMedicine.getTotalPrice()).id(reportMedicine.getId()).build();
            return basisResponseMapper.createBasisResponse(reportMedicineDto);
        }else {
            throw  new ResourceNotFoundException("Report_Medicine" ,"id" ,id);
        }

    }

    @Override
    public BasisResponse editeMedicineAmount(Long id, ReportMedicineAmountDto reportMedicineAmountDto) {
        Medicine oldMedicine = medicineRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Medicine","id",id));
        Report_Medicine reportMedicineOld = reportMedicineRepo.findByMedicineId(id);
        Report_Medicine reportMedicine =new Report_Medicine();
        if (reportMedicineOld!=null){
            int numberBox = reportMedicineAmountDto.getNumberBox();
            int numberPastille = reportMedicineAmountDto.getNumberPastille();
            double totalPrice = 0;
            numberBox += numberPastille / oldMedicine.getNumberOfPastilleInEachBox();
            numberPastille = numberPastille % oldMedicine.getNumberOfPastilleInEachBox();
            totalPrice = (numberBox*oldMedicine.getPrice())+ (((double) numberPastille /oldMedicine.getNumberOfPastilleInEachBox())*oldMedicine.getPrice());
            reportMedicineOld.setNumberBox(numberBox);
            reportMedicineOld.setNumberPastille(numberPastille);
            reportMedicineOld.setTotalPrice(totalPrice);
            reportMedicine = reportMedicineRepo.save(reportMedicineOld);
            ReportMedicineDto reportMedicineDto = ReportMedicineDto.builder().numberPastille(reportMedicine.getNumberPastille())
                    .medicineId(reportMedicine.getMedicineId()).medicine(oldMedicine.getNameInEng()).numberBox(reportMedicine.getNumberBox())
                    .totalPrice(reportMedicine.getTotalPrice()).id(reportMedicine.getId()).build();
            return basisResponseMapper.createBasisResponse(reportMedicineDto);
        }else {
            throw  new ResourceNotFoundException("Report_Medicine" ,"id" ,id);
        }
    }

    @Override
    public BasisResponse deleteMedicine(Long id) {
        Report_Medicine reportMedicineOld = reportMedicineRepo.findByMedicineId(id);
        Report_Medicine reportMedicine =new Report_Medicine();
        if (reportMedicineOld!=null){
            reportMedicineRepo.delete(reportMedicineOld);
            return basisResponseMapper.createBasisResponse("Medicine Deleted successfully");
        }else {
            throw  new ResourceNotFoundException("Report_Medicine" ,"id" ,id);
        }

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
