package it.matteobarbera.model.exceptions

class NotDiagonalException():
    RuntimeException("This matrix is not diagonal. To invert this matrix, use the ! operator.")