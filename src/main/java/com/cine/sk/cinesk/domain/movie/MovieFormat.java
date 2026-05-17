package com.cine.sk.cinesk.domain.movie;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Common video/container formats used by movies in the system.
 * Stored in the DB as a string (via @Enumerated(EnumType.STRING) on the entity).
 */
public enum MovieFormat {
    MP4,
    MKV,
    AVI,
    MOV,
    WMV,
    FLV,
    WEBM,
    HLS,   // HTTP Live Streaming (streaming protocol)
    DASH,  // MPEG-DASH (streaming protocol)
    UNKNOWN;

    /**
     * Lenient factory to convert user input or MIME-type/extension to enum.
     * Accepts values like "mp4", "video/mp4", "m3u8", case-insensitive.
     */
    @JsonCreator
    public static MovieFormat fromString(String value) {
        if (value == null) return UNKNOWN;
        String v = value.trim().toLowerCase();
        switch (v) {
            case "mp4":
            case "video/mp4":
                return MP4;
            case "mkv":
            case "video/x-matroska":
                return MKV;
            case "avi":
            case "video/x-msvideo":
                return AVI;
            case "mov":
            case "video/quicktime":
                return MOV;
            case "wmv":
            case "video/x-ms-wmv":
                return WMV;
            case "flv":
            case "video/x-flv":
                return FLV;
            case "webm":
            case "video/webm":
                return WEBM;
            case "hls":
            case "m3u8":
            case "application/vnd.apple.mpegurl":
            case "application/x-mpegurl":
                return HLS;
            case "dash":
            case "mpd":
            case "application/dash+xml":
                return DASH;
            case "unknown":
                return UNKNOWN;
            default:
                // Try direct enum match (case-insensitive)
                try {
                    return MovieFormat.valueOf(v.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return UNKNOWN;
                }
        }
    }
}


