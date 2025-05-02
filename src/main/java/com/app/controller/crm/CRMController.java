package com.app.controller.crm;


import com.app.entity.evaluation.Agent;
import com.app.entity.evaluation.Area;
import com.app.entity.evaluation.CustomerVisit;
import com.app.repository.evaluation.AgentRepository;
import com.app.repository.evaluation.AreaRepository;
import com.app.repository.evaluation.CustomerVisitRepository;
import com.app.service.TwilioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/crm")
//for lead generation
public class CRMController {

    private AreaRepository areaRepository;
    private AgentRepository agentRepository;
    private CustomerVisitRepository customerVisitRepository;
    private TwilioService twilioService;

    public CRMController(AreaRepository areaRepository,
                         AgentRepository agentRepository,
                         CustomerVisitRepository customerVisitRepository,
                         TwilioService twilioService){
        this.areaRepository = areaRepository;
        this.agentRepository = agentRepository;
        this.customerVisitRepository = customerVisitRepository;
        this.twilioService = twilioService;
    }

    //http://localhost:8080/api/v1/crm
    @GetMapping
    public ResponseEntity<List<Area> > searchArea(
            @RequestParam long pinCode
    ){
        List<Area> areas = areaRepository.findByPinCode(pinCode);
        return new ResponseEntity<> (areas, HttpStatus.OK);
    }

    //http://localhost:8080/api/v1/crm?customerId=1&agentId=1
    @PutMapping
    public String allocateAgent(
            @RequestParam long customerId,
            @RequestParam long agentId
    ){
        Agent agent = null;
        Optional<Agent> opAgent = agentRepository.findById(agentId);
        if(opAgent.isPresent()){
            agent = opAgent.get();
        }
        CustomerVisit customerVisit = customerVisitRepository.findById(customerId).get();
        customerVisit.setAgent(agent);
        customerVisitRepository.save(customerVisit);
        twilioService.sendSms("+918217542536","Agent is now Allocated - 123456");
        // give here the number given for siginin in twilio means this will send msg to customer who booked agent
        return "Agent is now Allocated";

    }
}
