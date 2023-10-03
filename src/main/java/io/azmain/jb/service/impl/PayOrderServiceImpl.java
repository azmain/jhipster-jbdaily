package io.azmain.jb.service.impl;

import io.azmain.jb.domain.PayOrder;
import io.azmain.jb.repository.PayOrderRepository;
import io.azmain.jb.service.PayOrderService;
import io.azmain.jb.service.dto.PayOrderDTO;
import io.azmain.jb.service.mapper.PayOrderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PayOrder}.
 */
@Service
@Transactional
public class PayOrderServiceImpl implements PayOrderService {

    private final Logger log = LoggerFactory.getLogger(PayOrderServiceImpl.class);

    private final PayOrderRepository payOrderRepository;

    private final PayOrderMapper payOrderMapper;

    public PayOrderServiceImpl(PayOrderRepository payOrderRepository, PayOrderMapper payOrderMapper) {
        this.payOrderRepository = payOrderRepository;
        this.payOrderMapper = payOrderMapper;
    }

    @Override
    public PayOrderDTO save(PayOrderDTO payOrderDTO) {
        log.debug("Request to save PayOrder : {}", payOrderDTO);
        PayOrder payOrder = payOrderMapper.toEntity(payOrderDTO);
        payOrder = payOrderRepository.save(payOrder);
        return payOrderMapper.toDto(payOrder);
    }

    @Override
    public PayOrderDTO update(PayOrderDTO payOrderDTO) {
        log.debug("Request to update PayOrder : {}", payOrderDTO);
        PayOrder payOrder = payOrderMapper.toEntity(payOrderDTO);
        payOrder = payOrderRepository.save(payOrder);
        return payOrderMapper.toDto(payOrder);
    }

    @Override
    public Optional<PayOrderDTO> partialUpdate(PayOrderDTO payOrderDTO) {
        log.debug("Request to partially update PayOrder : {}", payOrderDTO);

        return payOrderRepository
            .findById(payOrderDTO.getId())
            .map(existingPayOrder -> {
                payOrderMapper.partialUpdate(existingPayOrder, payOrderDTO);

                return existingPayOrder;
            })
            .map(payOrderRepository::save)
            .map(payOrderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PayOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PayOrders");
        return payOrderRepository.findAll(pageable).map(payOrderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PayOrderDTO> findOne(Long id) {
        log.debug("Request to get PayOrder : {}", id);
        return payOrderRepository.findById(id).map(payOrderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PayOrder : {}", id);
        payOrderRepository.deleteById(id);
    }
}
