package com.paul.shitment.shipment_service.models.enums;

public enum ShipmentStatus {
    REGISTERED,
    IN_TRANSIT, // Está en camino
    ARRIVED_AT_OFFICE, // Llegó a la oficina de destino
    WAITING_PICKUP, // Esperando a que el destinatario lo recoja
    DELIVERED,
    CANCELED
}
