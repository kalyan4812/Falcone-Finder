package com.example.falcone_finder.framework.utils

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> ComponentActivity.collectLifecycleAwareFlow(
    flow: Flow<T>,
    collect: suspend (T) -> Unit
) {
    lifecycleScope.launch {

        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }


    }
}

fun <T> ComponentActivity.collectLifecycleAwareChannelFlow(
    flow: Flow<T>,
    collect: FlowCollector<T>
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}

fun <T> Fragment.collectLifecycleAwareFlow(
    flow: Flow<T>,
    collect: suspend (T) -> Unit
) {
    lifecycleScope.launch {

        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }


    }
}

fun <T> Fragment.collectLifecycleAwareChannelFlow(
    flow: Flow<T>,
    collect: FlowCollector<T>
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collect)
        }
    }
}