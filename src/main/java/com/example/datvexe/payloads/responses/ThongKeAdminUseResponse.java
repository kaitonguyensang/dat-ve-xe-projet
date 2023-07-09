package com.example.datvexe.payloads.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ThongKeAdminUseResponse {
    private Long nhaXeId;
    private String tenNhaXe;
    private int soSuatVe;
    private int soSuatHangHoa;
    private float tyLeVe;
    private float tyLeHangHoa;
}
