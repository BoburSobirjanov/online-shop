package uz.com.onlineshop.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import uz.com.onlineshop.exception.DataNotFoundException;
import uz.com.onlineshop.model.entity.user.Gender;
import uz.com.onlineshop.model.entity.user.UserEntity;
import uz.com.onlineshop.model.entity.user.UserRole;
import uz.com.onlineshop.repository.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {


    private final UserRepository userRepository;






    public byte[] generateUserPdf(UUID id) {
        UserEntity user = userRepository.findUserEntityById(id);
        if (user==null){
            throw new DataNotFoundException("User not found!");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.add(new Paragraph("ARIZA").setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18)
                .setBold());
        document.add(new Paragraph("Ism va familyasi: " + user.getFullName()).setItalic());
        document.add(new Paragraph("Telefon raqami: " + user.getPhoneNumber()).setItalic());
        document.add(new Paragraph("Elektron pochtasi: " + user.getEmail()).setItalic());
        document.add(new Paragraph("Yashash manzili: " + user.getAddress()).setItalic());
        if (user.getGender().equals(Gender.MALE)){
            document.add(new Paragraph("Jinsi: Erkak").setItalic());
        }
        if (user.getGender().equals(Gender.FEMALE)){
            document.add(new Paragraph("Jinsi: Ayol").setItalic());
        }
        if (user.getRole().equals(UserRole.USER)){
            document.add(new Paragraph("Tizimdagi o'rni: Foydalanuvchi").setItalic());
        }
        if (user.getRole().equals(UserRole.ADMIN)){
            document.add(new Paragraph("Tizimdagi o'rni: Admin").setItalic());
        }
        if (user.getRole().equals(UserRole.OWNER)){
            document.add(new Paragraph("Tizimdagi o'rni: Direktor").setItalic());
        }
        document.add(new Paragraph("Holati: " + user.getUserStatus()).setItalic());
        document.add(new Paragraph("Foydalanuvchi nomi: " + user.getUsername()).setItalic());

        document.close();

        return out.toByteArray();
    }









    public byte[] generateUsersExcel(List<UserEntity> users) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Full name");
        headerRow.createCell(1).setCellValue("Phone number");
        headerRow.createCell(2).setCellValue("Email");
        headerRow.createCell(3).setCellValue("Address");
        headerRow.createCell(4).setCellValue("Gender");
        headerRow.createCell(5).setCellValue("Role");
        int rowNum = 1;
        for (UserEntity user : users) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(user.getFullName());
            row.createCell(1).setCellValue(user.getPhoneNumber());
            row.createCell(2).setCellValue(user.getEmail());
            row.createCell(3).setCellValue(user.getAddress());
            row.createCell(4).setCellValue(String.valueOf(user.getGender()));
            row.createCell(5).setCellValue(String.valueOf(user.getRole()));
        }
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }
}
