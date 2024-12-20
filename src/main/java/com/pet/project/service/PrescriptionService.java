package com.pet.project.service;

import com.pet.project.dto.prescription.CreatePrescriptionRequest;
import com.pet.project.dto.prescription.PaymentRequest;
import com.pet.project.dto.prescription.PrescriptionVO;

import java.util.List;



public interface PrescriptionService {

    /**
     * 创建处方
     * @param request 创建处方请求
     * @return 处方VO
     */
    PrescriptionVO createPrescription(CreatePrescriptionRequest request);

    /**
     * 获取处方详情
     * @param id 处方ID
     * @return 处方VO
     */
    PrescriptionVO getPrescriptionById(Long id);

    /**
     * 处理处方（发药）
     * @param id 处方ID
     * @return 处方VO
     */
    PrescriptionVO dispensePrescription(Long id);

    /**
     * 获取待处理的处方列表
     * @return 处方VO列表
     */
    List<PrescriptionVO> getPendingPrescriptions();

    /**
     * 获取医生的处方列表
     * @param doctorId 医生ID
     * @return 处方VO列表
     */
    List<PrescriptionVO> getDoctorPrescriptions(Long doctorId);

    /**
     * 获取宠物的处方列表
     * @param petId 宠物ID
     * @return 处方VO列表
     */
    List<PrescriptionVO> getPetPrescriptions(Long petId);
}
