package ssd.uz.wikiquickyapp.service;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ssd.uz.wikiquickyapp.entity.Order;
import ssd.uz.wikiquickyapp.entity.Role;
import ssd.uz.wikiquickyapp.entity.Users;
import ssd.uz.wikiquickyapp.entity.enums.RoleName;
import ssd.uz.wikiquickyapp.exception.ResourceNotFoundException;
import ssd.uz.wikiquickyapp.repository.OrderRepository;
import ssd.uz.wikiquickyapp.repository.RoleRepository;
import ssd.uz.wikiquickyapp.repository.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ExcelService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    /*****************************Users List******************************/

    private static String[] columnUsers = {"t/r", "Name", "Phone Number", "Roles", "Active", "Balance", "Points"};

    public ResponseEntity<byte[]> getUsers() {
        Workbook workbook = new XSSFWorkbook();
        CreationHelper creationHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("↑↑↑Users↑↑↑");

        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.AUTOMATIC.getIndex());
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Arial Black");

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);

        Row row = sheet.createRow(0);

        for (int i = 0; i < columnUsers.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(columnUsers[i]);
        }

        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        int rowNum = 1;
        List<Users> usersList = userRepository.findAll();

        int number = 1;

        CellStyle tr = workbook.createCellStyle();
        tr.setBorderTop(BorderStyle.THIN);
        tr.setBorderBottom(BorderStyle.THIN);
        tr.setBorderLeft(BorderStyle.THIN);
        tr.setBorderRight(BorderStyle.THIN);
        tr.setAlignment(HorizontalAlignment.CENTER_SELECTION);

        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        for (Users user : usersList) {
            Row row1 = sheet.createRow(rowNum++);

            Cell cell = row1.createCell(0);
            cell.setCellValue(number++);
            cell.setCellStyle(tr);

            Cell cel2 = row1.createCell(1);
            cel2.setCellValue(user.getFirstName() + " " + user.getLastName());
            cel2.setCellStyle(style);

            cel2 = row1.createCell(2);
            cel2.setCellValue(user.getPhoneNumber());
            cel2.setCellStyle(tr);

            cel2 = row1.createCell(3);
            String userRoles = "";
            List<Role> roles = user.getRoles();
            for (int i = 0; i < roles.size(); i++) {
                if (i != roles.size() - 1) {
                    userRoles = userRoles + roles.get(i).getName() + ", ";
                }
                userRoles = userRoles + roles.get(i).getName();
            }
            cel2.setCellValue(userRoles);
            cel2.setCellStyle(tr);

            cel2 = row1.createCell(4);
            cel2.setCellValue(user.getRoles().contains(roleRepository.findByName(RoleName.ROLE_BLOCKED).get()) ? "Activated" : "Blocked");
            cel2.setCellStyle(tr);

            cell = row1.createCell(5);
            cell.setCellValue(user.getBalance() == null ? 0 : user.getBalance());
            cell.setCellStyle(tr);

            cell = row1.createCell(6);
            cell.setCellValue(user.getPoints() == null ? 0 : user.getPoints());
            cell.setCellStyle(tr);

        }

        for (int i = 0; i < columnUsers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            workbook.write(byteArrayOutputStream);
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = byteArrayOutputStream.toByteArray();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openXmlFormats-officeDocument.spreadSheetMl.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\" COMPANY :" + currentDateTime + ".xlsx")
                .body(bytes);
    }

    /*****************************Orders List******************************/

    private static String[] columnOrders = {"t/r", "Client name", "Worker name", "Receiver number", "Status", "Order cost", "Description", "Rejected by"};

    public ResponseEntity<byte[]> getOrders() {
        Workbook workbook = new XSSFWorkbook();
        CreationHelper creationHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("↑↑↑Orders↑↑↑");

        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.AUTOMATIC.getIndex());
        font.setFontHeightInPoints((short) 11);
        font.setFontName("Arial Black");

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);

        Row row = sheet.createRow(0);

        for (int i = 0; i < columnOrders.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(columnOrders[i]);
        }

        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        int rowNum = 1;
        List<Order> ordersList = orderRepository.findAll();

        int number = 1;

        CellStyle tr = workbook.createCellStyle();
        tr.setBorderTop(BorderStyle.THIN);
        tr.setBorderBottom(BorderStyle.THIN);
        tr.setBorderLeft(BorderStyle.THIN);
        tr.setBorderRight(BorderStyle.THIN);
        tr.setAlignment(HorizontalAlignment.CENTER_SELECTION);

        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        for (Order order : ordersList) {
            Row row1 = sheet.createRow(rowNum++);

            Cell cell = row1.createCell(0);
            cell.setCellValue(number++);
            cell.setCellStyle(tr);

            Cell cel2 = row1.createCell(1);
            cel2.setCellValue(order.getClient().getFirstName() + " " + order.getClient().getLastName());
            cel2.setCellStyle(style);

            cel2 = row1.createCell(2);
            cel2.setCellValue(order.getWorker().getFirstName() + " " + order.getWorker().getLastName());
            cel2.setCellStyle(style);

            cel2 = row1.createCell(3);
            if (order.getOrderStatusEnum() != null) {
                cel2.setCellValue(order.getReceiverNumber());
            } else {
                cel2.setCellValue("null");
            }
            cel2.setCellStyle(tr);

            cel2 = row1.createCell(4);
            if (order.getOrderStatusEnum() != null) {
                cel2.setCellValue(order.getOrderStatusEnum().toString());
            } else {
                cel2.setCellValue("null");
            }
            cel2.setCellStyle(tr);

            cell = row1.createCell(5);
            if (order.getOrderStatusEnum() != null) {
                cell.setCellValue(order.getOrderCost());
            } else {
                cell.setCellValue("null");
            }
            cell.setCellStyle(tr);

            cell = row1.createCell(6);
            if (order.getOrderStatusEnum() != null) {
                cell.setCellValue(order.getDescription());
            } else {
                cell.setCellValue("null");
            }
            cell.setCellStyle(tr);

//            cell = row1.createCell(7);
//            Users rejectedUser = userRepository.findById(order.getRejectFrom()).orElseThrow(() -> new ResourceNotFoundException("user", "id", order.getRejectFrom()));
//            cell.setCellValue(rejectedUser.getFirstName() + " " + rejectedUser.getLastName());
//            cell.setCellStyle(tr);

        }

        for (int i = 0; i < columnUsers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            workbook.write(byteArrayOutputStream);
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = byteArrayOutputStream.toByteArray();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openXmlFormats-officeDocument.spreadSheetMl.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\" COMPANY :" + currentDateTime + ".xlsx")
                .body(bytes);
    }

}
