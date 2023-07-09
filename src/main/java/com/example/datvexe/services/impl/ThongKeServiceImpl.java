package com.example.datvexe.services.impl;

import com.example.datvexe.common.ChuyenXe;
import com.example.datvexe.common.TrangThai;
import com.example.datvexe.handler.CustomException;
import com.example.datvexe.models.*;
import com.example.datvexe.payloads.requests.ThongKeAdminRequest;
import com.example.datvexe.payloads.requests.ThongKeNhaXeRequest;
import com.example.datvexe.payloads.responses.*;
import com.example.datvexe.repositories.*;
import com.example.datvexe.services.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThongKeServiceImpl implements ThongKeService {

    @Autowired
    NhaXeRepository nhaXeRepository;

    @Autowired
    DanhGiaRepository danhGiaRepository;

    @Autowired
    VeXeRepository veXeRepository;

    @Autowired
    HangHoaRepository hangHoaRepository;

    @Autowired
    LoaiXeRepository loaiXeRepository;

    @Autowired
    TuyenXeRepository tuyenXeRepository;

    @Autowired
    XeRepository xeRepository;

    @Override
    public float tinhTrungBinhSao(Long nhaXeId) {
        NhaXe nhaXe = nhaXeRepository.findNhaXeById(nhaXeId);
        if (nhaXe == null) throw new CustomException("404", "Khong tim thay nha xe!!!");
        List<DanhGia> danhGiaList = danhGiaRepository.findDanhGiasByNhaXe_Id(nhaXe.getId());
        int temp = 0;
        for (DanhGia danhGia : danhGiaList) {
            temp = temp + danhGia.getSoSao();
        }
        int size = danhGiaList.size();
        float temp1 = Float.valueOf(temp);
        float ketqua = temp1 / size;
        return ketqua;
    }

    @Override
    public ThongKeSaoResponse thongKeSaoRequest(Long nhaXeId) {
        NhaXe nhaXe = nhaXeRepository.findNhaXeById(nhaXeId);
        if (nhaXe == null) throw new CustomException("404", "Khong tim thay nha xe!!!");
        List<DanhGia> danhGiaList = danhGiaRepository.findDanhGiasByNhaXe_Id(nhaXe.getId());
        ThongKeSaoResponse thongKeSaoRequest = new ThongKeSaoResponse();
        int a[] = new int[5];
        for (DanhGia danhGia : danhGiaList) {
            a[danhGia.getSoSao() - 1] = a[danhGia.getSoSao() - 1] + 1;
        }
        thongKeSaoRequest.setSao1(a[0]);
        thongKeSaoRequest.setSao2(a[1]);
        thongKeSaoRequest.setSao3(a[2]);
        thongKeSaoRequest.setSao4(a[3]);
        thongKeSaoRequest.setSao5(a[4]);
        thongKeSaoRequest.setSoNguoiDanhGia(danhGiaList.size());
        return thongKeSaoRequest;
    }

    @Override
    public List<SaoTrungBinhAllResponse> getSaoTrungBinhAll() {
        List<NhaXe> nhaXeList = nhaXeRepository.findAll();
        List<SaoTrungBinhAllResponse> saoTrungBinhAllResponseList = new ArrayList<SaoTrungBinhAllResponse>();
        for (NhaXe nhaXe : nhaXeList) {
            SaoTrungBinhAllResponse saoTrungBinh = new SaoTrungBinhAllResponse();
            saoTrungBinh.setSaoTrungBinh(tinhTrungBinhSao(nhaXe.getId()));
            saoTrungBinh.setId(nhaXe.getId());
            if (Float.isNaN(saoTrungBinh.getSaoTrungBinh())) saoTrungBinh.setSaoTrungBinh(0F);
            saoTrungBinhAllResponseList.add(saoTrungBinh);
        }
        return saoTrungBinhAllResponseList;
    }

    @Override
    public List<ThongKeAdminUseResponse> getThongKeAdminUse(ThongKeAdminRequest thongKeAdminRequest) {
        List<VeXe> veXeList = veXeRepository.findVeXeByTrangThaiOrTrangThai(TrangThai.ACTIVE,TrangThai.COMPLETED);
        List<VeXe> veXes = new ArrayList<VeXe>();
        for (VeXe veXe : veXeList) {
            if (veXe.getNgayDat().getMonthValue() == thongKeAdminRequest.getMonth() && veXe.getNgayDat().getYear() == thongKeAdminRequest.getYear()) {
                veXes.add(veXe);
            }
        }
        List<HangHoa> hangHoaList = hangHoaRepository.findHangHoaByTrangThaiOrTrangThai(TrangThai.ACTIVE, TrangThai.COMPLETED);
        List<HangHoa> hangHoas = new ArrayList<HangHoa>();
        List<ThongKeAdminUseResponse> thongKeAdminUseResponseList = new ArrayList<ThongKeAdminUseResponse>();
        for (HangHoa hangHoa : hangHoaList) {
            if (hangHoa.getNgayDat().getMonthValue() == thongKeAdminRequest.getMonth() && hangHoa.getNgayDat().getYear() == thongKeAdminRequest.getYear()) {
                hangHoas.add(hangHoa);
            }
        }
        List<NhaXe> nhaXeList = nhaXeRepository.findAll();
        Float tongVe = (float) veXes.size();
        Float tongHangHoa = (float) hangHoas.size();
        for (NhaXe nhaXe : nhaXeList) {
            int tempVe = 0;
            int tempHangHoa = 0;
            ThongKeAdminUseResponse thongKeAdminUseResponse = new ThongKeAdminUseResponse();
            thongKeAdminUseResponse.setNhaXeId(nhaXe.getId());
            thongKeAdminUseResponse.setTenNhaXe(nhaXe.getTenNhaXe());
            for (VeXe veXe : veXes) {
                if (nhaXe.getId() == veXe.getTuyenXe().getXe().getNhaXe().getId()) {
                    tempVe = tempVe + 1;
                }
            }
            for (HangHoa hangHoaCheck : hangHoas) {
                if (nhaXe.getId() == hangHoaCheck.getTuyenXe().getXe().getNhaXe().getId())
                    tempHangHoa = tempHangHoa + 1;
            }
            thongKeAdminUseResponse.setSoSuatVe(tempVe);
            thongKeAdminUseResponse.setSoSuatHangHoa(tempHangHoa);
            thongKeAdminUseResponse.setTyLeVe(( tempVe/ tongVe)*100);
            if (tempVe ==0) thongKeAdminUseResponse.setTyLeVe(0);
            thongKeAdminUseResponse.setTyLeHangHoa(( tempHangHoa/ tongHangHoa)*100);
            if (tempHangHoa ==0) thongKeAdminUseResponse.setTyLeHangHoa(0);
            thongKeAdminUseResponseList.add(thongKeAdminUseResponse);
        }
        return thongKeAdminUseResponseList;
    }

    @Override
    public List<ThongKeAdminDoanhThuResponse> getThongKeAdminDoanhThu(ThongKeAdminRequest thongKeAdminRequest) {
        List<VeXe> veXeList = veXeRepository.findVeXeByTrangThaiOrTrangThai(TrangThai.ACTIVE,TrangThai.COMPLETED);
        List<VeXe> veXes = new ArrayList<VeXe>();
        for (VeXe veXe : veXeList) {
            if (veXe.getNgayDat().getMonthValue() == thongKeAdminRequest.getMonth() && veXe.getNgayDat().getYear() == thongKeAdminRequest.getYear()) {
                veXes.add(veXe);
            }
        }
        List<HangHoa> hangHoaList = hangHoaRepository.findHangHoaByTrangThaiOrTrangThai(TrangThai.ACTIVE, TrangThai.COMPLETED);
        List<HangHoa> hangHoas = new ArrayList<HangHoa>();
        List<ThongKeAdminDoanhThuResponse> thongKeAdminDoanhThuResponseList = new ArrayList<ThongKeAdminDoanhThuResponse>();
        for (HangHoa hangHoa : hangHoaList) {
            if (hangHoa.getNgayDat().getMonthValue() == thongKeAdminRequest.getMonth() && hangHoa.getNgayDat().getYear() == thongKeAdminRequest.getYear()) {
                hangHoas.add(hangHoa);
            }
        }
        List<NhaXe> nhaXeList = nhaXeRepository.findAll();

        for (NhaXe nhaXe : nhaXeList) {
            int tempDoanhThu = 0;
            int tongDoanhThu=0;
            ThongKeAdminDoanhThuResponse thongKeAdminDoanhThuResponse = new ThongKeAdminDoanhThuResponse();
            thongKeAdminDoanhThuResponse.setNhaXeId(nhaXe.getId());
            thongKeAdminDoanhThuResponse.setTenNhaXe(nhaXe.getTenNhaXe());
            for (VeXe veXe : veXes) {
                if (nhaXe.getId() == veXe.getTuyenXe().getXe().getNhaXe().getId()) {
                    tempDoanhThu = tempDoanhThu + veXe.getTuyenXe().getGiaVe();
                }
                tongDoanhThu = tongDoanhThu + veXe.getTuyenXe().getGiaVe();
            }
            for (HangHoa hangHoaCheck : hangHoas) {
                if (nhaXe.getId() == hangHoaCheck.getTuyenXe().getXe().getNhaXe().getId())
                    tempDoanhThu = tempDoanhThu + hangHoaCheck.getGia();
                tongDoanhThu = tongDoanhThu + hangHoaCheck.getGia();
            }
            thongKeAdminDoanhThuResponse.setDoanhThu(tempDoanhThu);
            thongKeAdminDoanhThuResponse.setTyLeDoanhThu(((float) tempDoanhThu/(float) tongDoanhThu)*100);
            thongKeAdminDoanhThuResponseList.add(thongKeAdminDoanhThuResponse);
        }
        return thongKeAdminDoanhThuResponseList;
    }

    @Override
    public List<ThongKeNhaXeLoaiXeResponse> getThongKeNhaXeLoaiXe(ThongKeNhaXeRequest request) {
        List<VeXe> veXeList = veXeRepository.findVeXeByTrangThaiOrTrangThai(TrangThai.ACTIVE, TrangThai.COMPLETED);
        List<VeXe> veXeNhaXeThangList = new ArrayList<VeXe>();
        for (VeXe veXe : veXeList){
            if (veXe.getTuyenXe().getXe().getNhaXe().getId()==request.getNhaXeId() && veXe.getNgayDat().getMonthValue()==request.getMonth() && veXe.getNgayDat().getYear()==request.getYear())
                veXeNhaXeThangList.add(veXe);
        }
        List<HangHoa> hangHoaNhaXeList = hangHoaRepository.findHangHoaByTrangThaiOrTrangThai(TrangThai.ACTIVE, TrangThai.COMPLETED);
        List<HangHoa> hangHoaNhaXeThangList = new ArrayList<HangHoa>();
        for (HangHoa hangHoa : hangHoaNhaXeList)
            if (hangHoa.getTuyenXe().getXe().getNhaXe().getId()==request.getNhaXeId() && hangHoa.getNgayDat().getMonthValue()==request.getMonth() && hangHoa.getNgayDat().getYear()==request.getYear())
                hangHoaNhaXeThangList.add(hangHoa);
        List<Long> idLoaiXeList = new ArrayList<Long>();
        List<LoaiXe> loaiXeList = loaiXeRepository.findAll();
        List<Xe> xeNhaXeList = xeRepository.findXeByNhaXe_Id(request.getNhaXeId());
        for (LoaiXe loaiXe : loaiXeList){
            for (Xe xe : xeNhaXeList){
                if (loaiXe.getId() == xe.getLoaiXe().getId()) {
                    idLoaiXeList.add(loaiXe.getId());
                    break;
                }
            }
        }
        List<ThongKeNhaXeLoaiXeResponse> thongKeNhaXeLoaiXeResponseList = new ArrayList<ThongKeNhaXeLoaiXeResponse>();
        for (Long idLoaiXe : idLoaiXeList){
            int tempDoanhThu=0;
            int tongDoanhThu = 0;
            LoaiXe loaiXe = loaiXeRepository.findOneById(idLoaiXe);
            ThongKeNhaXeLoaiXeResponse thongKeNhaXeLoaiXeResponse = new ThongKeNhaXeLoaiXeResponse();
            thongKeNhaXeLoaiXeResponse.setLoaiXeId(loaiXe.getId());
            thongKeNhaXeLoaiXeResponse.setTenLoaiXe(loaiXe.getTenLoaiXe());
            for (VeXe veXe : veXeNhaXeThangList){
                if (veXe.getTuyenXe().getXe().getLoaiXe().getId()==loaiXe.getId()){
                    tempDoanhThu = tempDoanhThu + veXe.getTuyenXe().getGiaVe();
                }
                tongDoanhThu = tongDoanhThu + veXe.getTuyenXe().getGiaVe();
            }
            for (HangHoa hangHoa : hangHoaNhaXeThangList){
                if (hangHoa.getTuyenXe().getXe().getLoaiXe().getId()==loaiXe.getId()){
                    tempDoanhThu = tempDoanhThu + hangHoa.getGia();
                }
                tongDoanhThu = tongDoanhThu + hangHoa.getGia();
            }
            thongKeNhaXeLoaiXeResponse.setTyLe(((float)tempDoanhThu/tongDoanhThu) * 100);
            thongKeNhaXeLoaiXeResponse.setTongDoanhThu(tempDoanhThu);
            thongKeNhaXeLoaiXeResponseList.add(thongKeNhaXeLoaiXeResponse);
        }
        return thongKeNhaXeLoaiXeResponseList;
    }

    @Override
    public List<ThongKeNhaXeTuyenXeResponse> getThongKeNhaXeTuyenXe(ThongKeNhaXeRequest request) {
        List<VeXe> veXeList = veXeRepository.findVeXeByTrangThaiOrTrangThai(TrangThai.ACTIVE, TrangThai.COMPLETED);
        List<VeXe> veXeNhaXeThangList = new ArrayList<VeXe>();
        for (VeXe veXe : veXeList){
            if (veXe.getTuyenXe().getXe().getNhaXe().getId()==request.getNhaXeId() && veXe.getNgayDat().getMonthValue()==request.getMonth() && veXe.getNgayDat().getYear()==request.getYear())
                veXeNhaXeThangList.add(veXe);
        }
        List<HangHoa> hangHoaNhaXeList = hangHoaRepository.findHangHoaByTrangThaiOrTrangThai(TrangThai.ACTIVE, TrangThai.COMPLETED);
        List<HangHoa> hangHoaNhaXeThangList = new ArrayList<HangHoa>();
        for (HangHoa hangHoa : hangHoaNhaXeList)
            if (hangHoa.getTuyenXe().getXe().getNhaXe().getId()==request.getNhaXeId() && hangHoa.getNgayDat().getMonthValue()==request.getMonth() && hangHoa.getNgayDat().getYear()==request.getYear())
                hangHoaNhaXeThangList.add(hangHoa);

        List<TuyenXe> tuyenXeList = tuyenXeRepository.findTuyenXeByTrangThaiOrTrangThai(TrangThai.ACTIVE, TrangThai.COMPLETED);
        List<TuyenXe> tuyenXeNhaXeList = new ArrayList<TuyenXe>();
        for (TuyenXe tuyenXe : tuyenXeList)
            if (tuyenXe.getXe().getNhaXe().getId()== request.getNhaXeId())
                tuyenXeNhaXeList.add(tuyenXe);
        List<ChuyenXe> chuyenXeList = new ArrayList<ChuyenXe>();

        for (TuyenXe tuyenXe : tuyenXeNhaXeList){
            int temp = 0;
            for (ChuyenXe chuyenXe : chuyenXeList) {
                if (tuyenXe.getBenXeDi().getTinhThanh() == chuyenXe.getTinhThanhDi() && tuyenXe.getBenXeDen().getTinhThanh() == chuyenXe.getTinhThanhDen())  temp =1;
            }
            if (temp ==0){
                ChuyenXe chuyenXeCheck = new ChuyenXe();
                chuyenXeCheck.setTinhThanhDi(tuyenXe.getBenXeDi().getTinhThanh());
                chuyenXeCheck.setTinhThanhDen(tuyenXe.getBenXeDen().getTinhThanh());
                chuyenXeList.add(chuyenXeCheck);
            }
        }
        List<ThongKeNhaXeTuyenXeResponse> thongKeNhaXeTuyenXeResponseList = new ArrayList<ThongKeNhaXeTuyenXeResponse>();
        for (ChuyenXe chuyenXe : chuyenXeList){
            int tempDoanhThu=0;
            int tongDoanhThu = 0;
            ThongKeNhaXeTuyenXeResponse thongKeNhaXeTuyenXeResponse = new ThongKeNhaXeTuyenXeResponse();
            thongKeNhaXeTuyenXeResponse.setTinhThanhDi(chuyenXe.getTinhThanhDi());
            thongKeNhaXeTuyenXeResponse.setTinhThanhDen(chuyenXe.getTinhThanhDen());
            for (VeXe veXe : veXeNhaXeThangList){
                if (veXe.getTuyenXe().getBenXeDi().getTinhThanh()==chuyenXe.getTinhThanhDi() && veXe.getTuyenXe().getBenXeDen().getTinhThanh() == chuyenXe.getTinhThanhDen()){
                    tempDoanhThu = tempDoanhThu + veXe.getTuyenXe().getGiaVe();
                }
                tongDoanhThu = tongDoanhThu + veXe.getTuyenXe().getGiaVe();
            }
            for (HangHoa hangHoa : hangHoaNhaXeThangList){
                if (hangHoa.getTuyenXe().getBenXeDi().getTinhThanh()==chuyenXe.getTinhThanhDi() && hangHoa.getTuyenXe().getBenXeDen().getTinhThanh()==chuyenXe.getTinhThanhDen()){
                    tempDoanhThu = tempDoanhThu + hangHoa.getGia();
                }
                tongDoanhThu = tongDoanhThu + hangHoa.getGia();
            }
            thongKeNhaXeTuyenXeResponse.setTyLe(((float)tempDoanhThu/tongDoanhThu) * 100);
            thongKeNhaXeTuyenXeResponse.setTongDoanhThu(tempDoanhThu);
            thongKeNhaXeTuyenXeResponseList.add(thongKeNhaXeTuyenXeResponse);
        }
        return thongKeNhaXeTuyenXeResponseList;
    }

}

