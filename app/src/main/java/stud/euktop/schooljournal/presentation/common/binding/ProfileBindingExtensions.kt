package stud.euktop.schooljournal.presentation.common.binding

import androidx.fragment.app.Fragment
import stud.euktop.domain.model.user.Gender
import stud.euktop.domain.utils.validation.EmailValidator
import stud.euktop.domain.utils.validation.NameLetterOnlyValidator
import stud.euktop.domain.utils.validation.PhoneValidator
import stud.euktop.schooljournal.databinding.FragmentItemRegUserBinding
import stud.euktop.schooljournal.presentation.common.base.BaseState
import stud.euktop.schooljournal.presentation.common.base.BaseViewModel
import stud.euktop.schooljournal.presentation.common.utils.FocusTrack
import stud.euktop.schooljournal.presentation.common.utils.toMessageId
import java.util.Date

interface ProfileFormState {
    val lastName: NameLetterOnlyValidator
    val firstName: NameLetterOnlyValidator
    val surName: NameLetterOnlyValidator
    val gender: Gender?
    val birthDay: Date?
    val email: EmailValidator
    val phone: PhoneValidator
}

interface ProfileFormActions {
    fun firstNameSet(value: String)
    fun lastNameSet(value: String)
    fun surNameSet(value: String)
    fun emailSet(value: String)
    fun phoneSet(value: String)
    fun genderSet(value: Gender?)
    fun birthDaySet(value: Date?)
}

fun <STATE, VM> FragmentItemRegUserBinding.setupProfileForm(
    fragment: Fragment,
    focusTrack: FocusTrack,
    viewModel: VM
) where
        STATE : BaseState<STATE>,
        STATE : ProfileFormState,
        VM : BaseViewModel<STATE, *>,
        VM : ProfileFormActions {

    with(fragment) {
        bindForm(focusTrack, viewModel) {
            field(firstNameInput, { it.firstName }, viewModel::firstNameSet)
            field(lastNameInput, { it.lastName }, viewModel::lastNameSet)
            field(surNameInput, { it.surName }, viewModel::surNameSet)
            field(emailInput, { it.email }, viewModel::emailSet)
            field(phoneInput, { it.phone }, viewModel::phoneSet)
        }

        bindSelect(
            select = genderSelect,
            viewModel = viewModel,
            getter = { it.gender },
            toText = { it?.let { fragment.getString(it.toMessageId()) } ?: "" },
            onSelected = viewModel::genderSet
        )

        bindDate(
            input = birthDateInput,
            viewModel = viewModel,
            getter = { it.birthDay },
            setter = viewModel::birthDaySet
        )
    }
}