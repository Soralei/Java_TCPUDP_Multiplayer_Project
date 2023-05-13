package com.example.sockets.shared;

public enum ActionMapping {
    // Server tells the client what their player id is.
    SERVER_TCP_SYNC_PLAYER_ID,

    // Server updates the client with the position of an entity.
    SERVER_UDP_SYNC_ENTITY_POSITION,

    // Server wants to update the client with an entity so that the client knows about it.
    // If it is already known, instead update the position.
    SERVER_TCP_SYNC_ENTITY,

    // Server notifies the client of that an entity was removed.
    SERVER_TCP_REMOVE_ENTITY,

    // The client shares its local position with the server.
    CLIENT_UDP_SEND_LOCAL_POSITION,

    // The client lets the server know of its UDP port.
    CLIENT_TCP_SYNC_UDP_PORT,

    // The client wants to notify the server of that it's disconnecting.
    CLIENT_TCP_DISCONNECT
}
