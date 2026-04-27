package stud.euktop.schooljournal.presentation.main.teacher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import stud.euktop.schooljournal.databinding.FragmentTeacherClassesBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment

//stud.euktop.schooljournal.presentation.main.teacher.TeacherClassesFragment
class TeacherClassesFragment : BaseFragment<
        FragmentTeacherClassesBinding,
        TeacherClassesViewModel,
        TeacherClassesState,
        Unit
        >() {
    override fun inflateBinding(i: LayoutInflater, c: ViewGroup?) =
        FragmentTeacherClassesBinding.inflate(i, c, false)

    override val viewModel: TeacherClassesViewModel by viewModels()

    override fun setupUI() {
        // TODO: Setup UI
    }

    override fun updateState(state: TeacherClassesState) {
        // TODO: Update state
    }

    override fun updateEvent(event: Unit) {}
}