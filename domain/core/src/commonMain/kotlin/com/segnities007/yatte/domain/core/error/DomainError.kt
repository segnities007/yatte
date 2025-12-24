package com.segnities007.yatte.domain.core.error

/**
 * ドメイン層で発生するエラーの基底クラス
 */
sealed class DomainError(
    override val message: String,
    override val cause: Throwable? = null,
) : Exception(message, cause)

/**
 * エンティティが見つからない場合のエラー
 */
class EntityNotFoundError(
    entityName: String,
    id: String,
) : DomainError("$entityName が見つかりません: $id")

/**
 * バリデーションエラー
 */
class ValidationError(
    message: String,
) : DomainError(message)

/**
 * 操作が許可されていない場合のエラー
 */
class OperationNotAllowedError(
    message: String,
) : DomainError(message)
