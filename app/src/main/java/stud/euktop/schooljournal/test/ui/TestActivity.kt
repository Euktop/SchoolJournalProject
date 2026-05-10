package stud.euktop.schooljournal.test.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.databinding.ActivityTestBinding
import stud.euktop.schooljournal.presentation.common.utils.DomainSelectHelper
import stud.euktop.schooljournal.test.ui.pagination.auto.AutoPaginationFragment
import stud.euktop.schooljournal.test.ui.pagination.manual.ManualPaginationFragment
import stud.euktop.uikit.components.input.select.ListSafe
import stud.euktop.uikit.components.input.select.def.SchJSelect

class TestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestBinding
    private lateinit var selectTest: SchJSelect

    // Список тестов (название и соответствующий фрагмент)
    private data class TestItem(
        val title: String,
        val fragment: () -> androidx.fragment.app.Fragment
    )

    private val testItems = listOf(
        TestItem("Ручная пагинация") { ManualPaginationFragment() },
        TestItem("Авто пагинация (Paging 3)") { AutoPaginationFragment() }
        // Сюда можно добавлять новые тесты позже
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectTest = binding.selectTest

        // Настраиваем выпадающий список
        val listSafe = ListSafe(
            values = testItems,
            toText = { it?.title ?: "" },
            onClick = { selectedTitle, _ ->
                val test = testItems.find { it == selectedTitle }
                test?.let {
                    supportFragmentManager.commit {
                        replace(R.id.fragmentContainer, it.fragment.invoke())
                    }
                }
            }
        )

        // Регистрируем RegisterList для SchJSelect
        val register = selectTest.RegisterList(listSafe)
        register.register(supportFragmentManager)
    }
}