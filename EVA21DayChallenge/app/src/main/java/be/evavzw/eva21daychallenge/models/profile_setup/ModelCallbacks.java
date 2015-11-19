package be.evavzw.eva21daychallenge.models.profile_setup;

/**
 * Callback interface connecting {@link Page}, {@link AbstractWizardModel}, and model container
 * objects (e.g. {@link be.evavzw.eva21daychallenge.activity.profile_setup.ProfileSetup}.
 */
public interface ModelCallbacks {
    void onPageDataChanged(Page page);

    void onPageTreeChanged();
}
