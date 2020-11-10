from enum import IntEnum

class StatusCode(IntEnum):
    SUCCESS = 0
    INSUFFICIENT_ARGS = 1
    ALREADY_REGISTERED = 2
    ALREADY_LOGGED_IN = 3
    VALIDATION_ERR = 4