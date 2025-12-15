package bg.mironov.bogdan.backend.controller.train;

import bg.mironov.bogdan.backend.dto.request.TrainingRequest;
import bg.mironov.bogdan.backend.dto.response.TrainingResponse;
import bg.mironov.bogdan.backend.service.train.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping("/run")
    public ResponseEntity<TrainingResponse> run(@RequestBody TrainingRequest request) {
        return ResponseEntity.ok(
            trainingService.run(
                request.symbol(),
                request.from(),
                request.to(),
                request.initialBalance()
            ));
    }
}
