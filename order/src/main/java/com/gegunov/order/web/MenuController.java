package com.gegunov.order.web;

import com.gegunov.order.jpa.model.MenuItem;
import com.gegunov.order.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody MenuItem menuItem) {
        UUID id = menuItemService.save(menuItem);
        return ResponseEntity.ok(id);
    }

    @GetMapping
    public ResponseEntity<Iterable<MenuItem>> getAll() {
        return ResponseEntity.ok(menuItemService.findAll());
    }
}
