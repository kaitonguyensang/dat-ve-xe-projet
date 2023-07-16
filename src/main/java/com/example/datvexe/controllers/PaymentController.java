package com.example.datvexe.controllers;

import com.example.datvexe.common.CommonApiService;
import com.example.datvexe.common.TrangThai;
import com.example.datvexe.config.Config;
import com.example.datvexe.models.NhaXe;
import com.example.datvexe.models.VeXe;
import com.example.datvexe.payloads.dto.ApiMessageDto;
import com.example.datvexe.payloads.requests.LePhiRequest;
import com.example.datvexe.payloads.requests.ThanhToanDatVeRequest;
import com.example.datvexe.repositories.NhaXeRepository;
import com.example.datvexe.repositories.VeXeRepository;
import com.example.datvexe.utils.CurrencyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin(origins = { "http://localhost:3000", "https://duyvotruong.github.io" })
@RequestMapping("/api/thanh-toan" )
public class PaymentController {

    @Autowired
    NhaXeRepository nhaXeRepository;

    @Autowired
    VeXeRepository veXeRepository;

    @Autowired
    CommonApiService commonApiService;

    @PostMapping(value = "/le-phi", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> thanhToanLePhi(@RequestBody LePhiRequest lePhiRequest, HttpServletRequest request) throws
            IOException {

        NhaXe nhaXe = nhaXeRepository.findNhaXesByEmail(lePhiRequest.getEmail());

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = lePhiRequest.getSoTien()*100;
        String bankCode = "NCB";

        String vnp_TxnRef = Config.getRandomNumber(8);
        nhaXe.setMaThanhToan(vnp_TxnRef);
        nhaXeRepository.save(nhaXe);

        String vnp_IpAddr = Config.getIpAddress(request);
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", bankCode);

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = request.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_Returnurl_Chargefee);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        //com.google.gson.JsonObject job = new JsonObject();
        //job.addProperty("code", "00");
        //job.addProperty("message", "success");
        //job.addProperty("data", paymentUrl);
        //Gson gson = new Gson();
        //resp.getWriter().write(gson.toJson(job));
        apiMessageDto.setMessage("Request payment success");
        apiMessageDto.setData(paymentUrl);
        apiMessageDto.setResult(true);
        return apiMessageDto;
    }

    @GetMapping("/le-phi/thong-tin-thanh-toan")
    public void transactionLePhi(HttpServletRequest request, HttpServletResponse resp,
            @RequestParam(value = "vnp_TxnRef") String vnp_TxnRef,
            @RequestParam(value = "vnp_Amount") String vnp_Amount,
            @RequestParam(value = "vnp_OrderInfo") String vnp_OrderInfo,
            @RequestParam(value = "vnp_ResponseCode") String vnp_ResponseCode,
            @RequestParam(value = "vnp_TransactionNo") String vnp_TransactionNo,
            @RequestParam(value = "vnp_BankCode") String vnp_BankCode,
            @RequestParam(value = "vnp_PayDate") String vnp_PayDate,
            @RequestParam(value = "vnp_TransactionStatus") String vnp_TransactionStatus,
            @RequestParam(value = "vnp_CardType") String vnp_CardType
    ) throws IOException {

        //tao chu ky so
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
            String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = Config.hashAllFields(fields);

        String status;
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(vnp_TransactionStatus)){
                status = "success";
                NhaXe nhaXe = nhaXeRepository.findNhaXeByMaThanhToan(vnp_TxnRef);
                Integer soThang = 0;
                if ((Long.parseLong(vnp_Amount)/100) == 2000000) {
                    nhaXe.setNgayHetHan(nhaXe.getNgayHetHan().plusMonths(1));
                    soThang=1;
                } else if ((Long.parseLong(vnp_Amount)/100) == 5000000) {
                    nhaXe.setNgayHetHan(nhaXe.getNgayHetHan().plusMonths(3));
                    soThang=3;
                } else if ((Long.parseLong(vnp_Amount)/100) == 9000000) {
                    nhaXe.setNgayHetHan(nhaXe.getNgayHetHan().plusMonths(6));
                    soThang=6;
                } else if ((Long.parseLong(vnp_Amount)/100 == 16000000)) {
                    nhaXe.setNgayHetHan(nhaXe.getNgayHetHan().plusMonths(12));
                    soThang=12;
                }
                String html = getHtmlContentForTraPhi(vnp_TxnRef, nhaXe, Integer.parseInt(vnp_Amount)/100, soThang);
                commonApiService.sendEmail(nhaXe.getEmail() , html,"Xác nhận đơn hàng",true);

            } else {
                status = "failed";
            }

        } else {
            status = "invalid signature";
        }

        resp.sendRedirect("http://localhost:3000/Dat-ve-xe-front-end-KLTN#/?ma-thanh-toan=1&trang-thai="+status);
    }

    @PostMapping(value = "/dat-ve", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> thanhToanDatVe(@RequestBody ThanhToanDatVeRequest thanhToanDatVeRequest, HttpServletRequest request) throws
            IOException {

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = thanhToanDatVeRequest.getTongSoTien()*100;
        String bankCode = "NCB";

        String vnp_TxnRef = Config.getRandomNumber(8);
        for (Integer soGhe : thanhToanDatVeRequest.getSoGheList()) {
            VeXe veXe = veXeRepository.findVeXeByTuyenXe_IdAndSoGhe(thanhToanDatVeRequest.getTuyenXeId(), soGhe);
            veXe.setMaThanhToan(vnp_TxnRef);
            veXeRepository.save(veXe);
        }

        String vnp_IpAddr = Config.getIpAddress(request);
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", bankCode);

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = request.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_Returnurl_Bookticket);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        //com.google.gson.JsonObject job = new JsonObject();
        //job.addProperty("code", "00");
        //job.addProperty("message", "success");
        //job.addProperty("data", paymentUrl);
        //Gson gson = new Gson();
        //resp.getWriter().write(gson.toJson(job));
        apiMessageDto.setMessage("Request payment success");
        apiMessageDto.setData(paymentUrl);
        apiMessageDto.setResult(true);
        return apiMessageDto;
    }

    @GetMapping("/hang-hoa/thong-tin-thanh-toan")
    public void transactionHangHoa(HttpServletRequest request, HttpServletResponse resp,
            @RequestParam(value = "vnp_TxnRef") String vnp_TxnRef,
            @RequestParam(value = "vnp_Amount") String vnp_Amount,
            @RequestParam(value = "vnp_OrderInfo") String vnp_OrderInfo,
            @RequestParam(value = "vnp_ResponseCode") String vnp_ResponseCode,
            @RequestParam(value = "vnp_TransactionNo") String vnp_TransactionNo,
            @RequestParam(value = "vnp_BankCode") String vnp_BankCode,
            @RequestParam(value = "vnp_PayDate") String vnp_PayDate,
            @RequestParam(value = "vnp_TransactionStatus") String vnp_TransactionStatus,
            @RequestParam(value = "vnp_CardType") String vnp_CardType
    ) throws IOException {

        //tao chu ky so
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
            String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = Config.hashAllFields(fields);

        String status;
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(vnp_TransactionStatus)){
                status = "success";
                List<VeXe> veXeList = veXeRepository.findVeXesByMaThanhToan(vnp_TxnRef);
                for (VeXe veXe : veXeList) {
                    veXe.setTrangThai(TrangThai.ACTIVE);
                    veXeRepository.save(veXe);
                }
            } else {
                status = "failed";
            }

        } else {
            status = "invalid signature";
        }

        resp.sendRedirect("http://localhost:3000/Dat-ve-xe-front-end-KLTN#/?ma-thanh-toan=2&trang-thai="+status);
    }

    @PostMapping(value = "/hang-hoa", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<String> thanhToanHangHoa(@RequestBody ThanhToanDatVeRequest thanhToanDatVeRequest, HttpServletRequest request) throws
            IOException {

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = thanhToanDatVeRequest.getTongSoTien()*100;
        String bankCode = "NCB";

        String vnp_TxnRef = Config.getRandomNumber(8);
        for (Integer soGhe : thanhToanDatVeRequest.getSoGheList()) {
            VeXe veXe = veXeRepository.findVeXeByTuyenXe_IdAndSoGhe(thanhToanDatVeRequest.getTuyenXeId(), soGhe);
            veXe.setMaThanhToan(vnp_TxnRef);
            veXeRepository.save(veXe);
        }

        String vnp_IpAddr = Config.getIpAddress(request);
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", bankCode);

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = request.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_Returnurl_Bookticket);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
        //com.google.gson.JsonObject job = new JsonObject();
        //job.addProperty("code", "00");
        //job.addProperty("message", "success");
        //job.addProperty("data", paymentUrl);
        //Gson gson = new Gson();
        //resp.getWriter().write(gson.toJson(job));
        apiMessageDto.setMessage("Request payment success");
        apiMessageDto.setData(paymentUrl);
        apiMessageDto.setResult(true);
        return apiMessageDto;
    }

    @GetMapping("/dat-ve/thong-tin-thanh-toan")
    public void transactionDatVe(HttpServletRequest request, HttpServletResponse resp,
            @RequestParam(value = "vnp_TxnRef") String vnp_TxnRef,
            @RequestParam(value = "vnp_Amount") String vnp_Amount,
            @RequestParam(value = "vnp_OrderInfo") String vnp_OrderInfo,
            @RequestParam(value = "vnp_ResponseCode") String vnp_ResponseCode,
            @RequestParam(value = "vnp_TransactionNo") String vnp_TransactionNo,
            @RequestParam(value = "vnp_BankCode") String vnp_BankCode,
            @RequestParam(value = "vnp_PayDate") String vnp_PayDate,
            @RequestParam(value = "vnp_TransactionStatus") String vnp_TransactionStatus,
            @RequestParam(value = "vnp_CardType") String vnp_CardType
    ) throws IOException {

        //tao chu ky so
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
            String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = Config.hashAllFields(fields);

        String status;
        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(vnp_TransactionStatus)){
                status = "success";
                List<VeXe> veXeList = veXeRepository.findVeXesByMaThanhToan(vnp_TxnRef);
                for (VeXe veXe : veXeList) {
                    veXe.setTrangThai(TrangThai.ACTIVE);
                    veXeRepository.save(veXe);
                }
                String html = getHtmlContentForDatVe(vnp_TxnRef, veXeList, Integer.parseInt(vnp_Amount)/100);
                commonApiService.sendEmail(veXeList.get(1).getUser().getEmail() , html,"Xác nhận đơn hàng",true);
            } else {
                status = "failed";
            }

        } else {
            status = "invalid signature";
        }

        resp.sendRedirect("http://localhost:3000/Dat-ve-xe-front-end-KLTN#/?ma-thanh-toan=2&trang-thai="+status);
    }

    private String getHtmlContentForDatVe(String code, List<VeXe> veXeList, Integer tongTien) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><body>");
        htmlBuilder.append("<h1>Thông tin đơn hàng</h1>");
        htmlBuilder.append("<div>");
        htmlBuilder.append("Mã đơn hàng: ");
        htmlBuilder.append("<b>").append(code).append("</b>");
        htmlBuilder.append("</div>");
        // Add a wrapper div to hold the table and area
        htmlBuilder.append("<div>");
        htmlBuilder.append("<table style=\"border-collapse: collapse; margin-bottom: 20px;\">");

        // Add table rows with order details
        htmlBuilder.append("<tr style=\"text-align: center;\">");
        htmlBuilder.append("<th style=\"border: 1px solid black; padding: 10px;\">Sản phẩm</th>");
        htmlBuilder.append("<th style=\"border: 1px solid black; padding: 10px;\">Số xe</th>");
        htmlBuilder.append("<th style=\"border: 1px solid black; padding: 10px;\">Số ghế</th>");
        htmlBuilder.append("<th style=\"border: 1px solid black; padding: 10px;\">Thành tiền</th>");
        htmlBuilder.append("</tr>");
        for (VeXe item : veXeList) {
            htmlBuilder.append("<tr style=\"text-align: center;\">");
            htmlBuilder.append("<td style=\"border: 1px solid black; padding: 10px;\">").append("Vé xe nhà xe " + item.getTuyenXe().getXe().getNhaXe().getTenNhaXe()).append("</td>");
            htmlBuilder.append("<td style=\"border: 1px solid black; padding: 10px;\">").append(item.getTuyenXe().getXe().getBienSoXe()).append("</td>");
            htmlBuilder.append("<td style=\"border: 1px solid black; padding: 10px;\">").append(item.getSoGhe()).append("</td>");
            htmlBuilder.append("<td style=\"border: 1px solid black; padding: 10px;\">").append(CurrencyUtils.convertCurrency((double) item.getTuyenXe().getGiaVe())).append("</td>");
            htmlBuilder.append("</tr>");
        }
        htmlBuilder.append("</table>");

        // Calculate the width of the table
        int tableWidth = veXeList.size() * 150; // Assuming each column has a width of 150px

        htmlBuilder.append("<div style=\"display: flex; justify-content: end;\">");
        htmlBuilder.append("<div>Tổng: ").append("<b>").append(CurrencyUtils.convertCurrency((double) tongTien)).append("</b>").append("</div>");
        htmlBuilder.append("</div>"); // Close the container div

        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

    private String getHtmlContentForTraPhi(String code, NhaXe nhaXe, Integer tongTien, Integer soThang) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><body>");
        htmlBuilder.append("<h1>Thông tin đơn hàng</h1>");
        htmlBuilder.append("<div>");
        htmlBuilder.append("Mã đơn hàng: ");
        htmlBuilder.append("<b>").append(code).append("</b>");
        htmlBuilder.append("</div>");
        // Add a wrapper div to hold the table and area
        htmlBuilder.append("<div>");
        htmlBuilder.append("<table style=\"border-collapse: collapse; margin-bottom: 20px;\">");

        // Add table rows with order details
        htmlBuilder.append("<tr style=\"text-align: center;\">");
        htmlBuilder.append("<th style=\"border: 1px solid black; padding: 10px;\">Chức năng thanh toán</th>");
        htmlBuilder.append("<th style=\"border: 1px solid black; padding: 10px;\">Số tháng</th>");
        htmlBuilder.append("<th style=\"border: 1px solid black; padding: 10px;\">Ngày hết hạn</th>");
        htmlBuilder.append("<th style=\"border: 1px solid black; padding: 10px;\">Thành tiền</th>");
        htmlBuilder.append("</tr>");
        htmlBuilder.append("<tr style=\"text-align: center;\">");
        htmlBuilder.append("<td style=\"border: 1px solid black; padding: 10px;\">").append("Trả phí đăng ký nhà xe hệ thống WEBTOUR.").append("</td>");
        htmlBuilder.append("<td style=\"border: 1px solid black; padding: 10px;\">").append(soThang).append("</td>");
        htmlBuilder.append("<td style=\"border: 1px solid black; padding: 10px;\">").append(nhaXe.getNgayHetHan()).append("</td>");
        htmlBuilder.append("<td style=\"border: 1px solid black; padding: 10px;\">").append(tongTien).append("</td>");
        htmlBuilder.append("</tr>");
        htmlBuilder.append("</table>");

        // Calculate the width of the table
        int tableWidth = 1 * 150; // Assuming each column has a width of 150px

        htmlBuilder.append("<div style=\"display: flex; justify-content: end;\">");
        htmlBuilder.append("<div>Tổng: ").append("<b>").append(CurrencyUtils.convertCurrency((double) tongTien)).append("</b>").append("</div>");
        htmlBuilder.append("</div>"); // Close the container div

        htmlBuilder.append("</body></html>");

        return htmlBuilder.toString();
    }

}
