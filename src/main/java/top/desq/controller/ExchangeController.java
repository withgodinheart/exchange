package top.desq.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.desq.dto.ResponseDto;
import top.desq.service.ExchangeService;
import top.desq.util.TimeUtil;

@RestController
@RequestMapping("/rates")
@RequiredArgsConstructor
@Slf4j
public class ExchangeController {

    private final ExchangeService rateService;

    @GetMapping("/usd/uah")
    public ResponseEntity<ResponseDto> showRates() {
        var current = rateService.getCurrentRate();
        var average = rateService.getAverageRate();
        var msg = "Текущий курс: %s. Средний курс: %s. Дата: %s".formatted(current, average, TimeUtil.getFormattedDate());
        return ResponseEntity.ok(new ResponseDto("success", msg));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ResponseDto> handleException(Exception e) {
        log.info("Exception thrown: {}", e.getMessage());
        return ResponseEntity.ok(new ResponseDto("error", e.getMessage()));
    }
}
