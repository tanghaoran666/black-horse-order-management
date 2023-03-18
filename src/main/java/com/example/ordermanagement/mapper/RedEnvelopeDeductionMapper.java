package com.example.ordermanagement.mapper;

import com.example.ordermanagement.domainModel.RedEnvelopeDeductionModel;
import com.example.ordermanagement.dto.RedEnvelopeDeductionRequest;
import com.example.ordermanagement.entity.RedEnvelopeDeduction;
import com.example.ordermanagement.enums.DeductionStatus;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RedEnvelopeDeductionMapper {
    RedEnvelopeDeductionMapper RED_ENVELOPE_DEDUCTION_MAPPER = Mappers.getMapper(RedEnvelopeDeductionMapper.class);
    RedEnvelopeDeductionModel toRedEnvelopeDeductionModel(RedEnvelopeDeductionRequest redEnvelopeDeductionRequest);
    RedEnvelopeDeduction toRedEnvelopeDeduction(RedEnvelopeDeductionModel redEnvelopeDeductionModel,
                                                String orderId,
                                                String failReason,
                                                DeductionStatus status);
}
