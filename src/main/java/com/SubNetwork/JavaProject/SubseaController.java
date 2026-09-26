package com.SubNetwork.JavaProject;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
public class SubseaController {
    private final SubseaService service;

    public SubseaController(SubseaService service) {
        this.service = service;
    }

    @GetMapping("/api/masters")
    public Map<String, Object> getMasters() {
        return service.getMasters();
    }

    @PostMapping("/api/contacts")
    public Map<String, String> addContact(@RequestBody Map<String, String> req) {
        service.addContact(req.get("name"), req.get("role"));
        return Map.of("status", "Contact saved successfully");
    }

    @PostMapping("/api/products")
    public Map<String, String> addProduct(@RequestBody Map<String, Object> req) {
        service.addProduct((String) req.get("name"), Double.parseDouble(req.get("price").toString()));
        return Map.of("status", "Product saved successfully");
    }

    @GetMapping("/api/operations")
    public List<Map<String, Object>> getOperations() {
        return service.getOperations();
    }

    @PostMapping("/api/operations")
    public Map<String, String> dispatchOperation(@RequestBody Map<String, Object> req) {
        service.ingestOtdrAndDispatch(
                (String) req.get("cableId"),
                Double.parseDouble(req.get("faultKm").toString()),
                (String) req.get("auvName"),
                Double.parseDouble(req.get("diveHours").toString())
        );
        return Map.of("status", "OTDR fault ingested and AUV dispatched successfully");
    }

    @DeleteMapping("/api/operations/{id}")
    public Map<String, String> deleteOperation(@PathVariable int id) {
        service.deleteOperation(id);
        return Map.of("status", "Operation record deleted");
    }

    @PostMapping("/api/sales-flow")
    public Map<String, Object> runSalesFlow(@RequestBody Map<String, Object> req) {
        return service.runSalesFlow(
                (String) req.get("customer"),
                (String) req.get("product"),
                Double.parseDouble(req.get("amount").toString()),
                (String) req.getOrDefault("actor", "Invoicing User")
        );
    }

    @PostMapping("/api/purchase-flow")
    public Map<String, Object> runPurchaseFlow(@RequestBody Map<String, Object> req) {
        return service.runPurchaseFlow(
                (String) req.get("vendor"),
                (String) req.get("item"),
                Double.parseDouble(req.get("amount").toString()),
                (String) req.getOrDefault("actor", "Admin")
        );
    }

    @GetMapping("/api/journals")
    public List<Map<String, Object>> getJournals() {
        return service.getJournals();
    }

    @DeleteMapping("/api/journals/{id}")
    public Map<String, String> deleteJournal(@PathVariable int id) {
        service.deleteJournal(id);
        return Map.of("status", "Journal entry removed");
    }

    @GetMapping("/api/reports")
    public Map<String, Object> getReports() {
        return service.getReports();
    }
}
