package it.matteobarbera.model.exceptions

class NormNotSupportedException(msg: String): RuntimeException(msg) {
    constructor(normValue: Double):
            this("Sorry, norm ${normValue} is not supported yet. Operation required is ignored.")
}