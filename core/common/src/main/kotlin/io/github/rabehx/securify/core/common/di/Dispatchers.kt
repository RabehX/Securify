package io.github.rabehx.securify.core.common.di

import javax.inject.Qualifier

/** IO-bound work: network, disk, DataStore. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

/** CPU-bound work: Rei native scans, parsing, aggregation. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

/** Process-lifetime scope for fire-and-forget collectors. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope
