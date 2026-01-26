package com.eb.eventsbridge.modules.core.entity;

public enum ClubRole {
    OWNER,   // Can delete/update/create a club, add/remove admins
    ADMIN,   // Can create/edit/delete events
    MEMBER   // Can just view internal club details
}
