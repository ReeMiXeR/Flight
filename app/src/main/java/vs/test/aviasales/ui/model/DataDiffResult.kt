package vs.test.aviasales.ui.model

import androidx.recyclerview.widget.DiffUtil

data class DataDiffResult<out T>(
    val data: T,
    val result: DiffUtil.DiffResult? = null
)