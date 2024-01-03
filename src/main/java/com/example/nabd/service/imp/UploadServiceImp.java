package com.example.nabd.service.imp;

import com.example.nabd.entity.Medicine;
import com.example.nabd.enums.MedicineStatus;
import com.example.nabd.exception.NabdAPIExeption;
import com.example.nabd.repository.MedicineRepo;
import com.example.nabd.service.UploadService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UploadServiceImp implements UploadService {
    private final MedicineRepo medicineRepo;

    public UploadServiceImp(MedicineRepo medicineRepo) {
        this.medicineRepo = medicineRepo;
    }
    @Override
    public ResponseEntity<Object> uploadDataFromExcelFile(MultipartFile file) {
        MedicineStatus medicineStatus = MedicineStatus.NOT_UPDATED;
        medicineRepo.updateMedicineByMedicineStatus(medicineStatus);
        List<List<String>> rows = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Set<Medicine> medicines = new TreeSet<>();
            int ii=0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                List<String> rowData = new ArrayList<>();
                Iterator<Cell> cellIterator = row.cellIterator();
                int index = 0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    String cellValue = getCellValueAsString(cell );
                    rowData.add(cellValue);
                    if (index==2||index==3){
                        if (!isDouble(cellValue)&&ii>0){
                            throw new NabdAPIExeption("format error"+" in row "+(ii-1),HttpStatus.BAD_REQUEST);
                        }
                    }
                    index++;
                }
                rows.add(rowData);
                ii++;
            }
            rows.remove(0);
            int i =0;
            for (List<String> row: rows) {
                i++;
                if (row.size()<4) continue;
                String nameInEng = row.get(0);
                String nameInArabic = row.get(1);
                Double price = Double.valueOf(row.get(2));
                Double numberOfPastilleInEachBox = Double.valueOf(row.get(3));
                String activeSubstance = row.size()<5 ? "null" : row.get(4);
                if (activeSubstance.length()>255) activeSubstance.substring(0,255);
                if (nameInEng==null||price<0||numberOfPastilleInEachBox<0){
                    throw new NabdAPIExeption("format error"+" in row "+i,HttpStatus.BAD_REQUEST);
                }
                Medicine medicine = medicineRepo.findByNameInEng(nameInEng);
                if (medicine!=null){
                    medicine.setPrice(price);
                    medicine.setNumberOfPastilleInEachBox(numberOfPastilleInEachBox.intValue());
                    medicine.setMedicineStatus(MedicineStatus.UPDATED);
//                    medicineRepo.save(medicine);
                    medicines.add(medicine);
                }else {
                    medicine = Medicine.builder().medicineStatus(MedicineStatus.UPDATED)
                            .activeSubstance(activeSubstance).price(price)
                            .nameInEng(nameInEng).nameInArb(nameInArabic)
                            .numberOfPastilleInEachBox(numberOfPastilleInEachBox.intValue()).build();
//                    medicineRepo.save(medicine);
                    medicines.add(medicine);
                }
            }
            medicineRepo.saveAll(medicines);
//            medicineRepo.saveAll(medicines);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String getCellValueAsString(Cell cell ) {
        String cellValue = "";
        if (cell.getCellType() == CellType.STRING.STRING) {
            cellValue = cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC  ) {
            cellValue = String.valueOf(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            cellValue = String.valueOf(cell.getBooleanCellValue());
        }
        return cellValue;
    }
    private boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void makeAllMedicineNotUpdated(){
        List<Medicine> medicines = medicineRepo.findAll();
        for (Medicine medicine:medicines) {
            medicine.setMedicineStatus(MedicineStatus.NOT_UPDATED);
//            medicineRepo.save(medicine);
        }
        medicineRepo.saveAll(medicines);
    }
}

