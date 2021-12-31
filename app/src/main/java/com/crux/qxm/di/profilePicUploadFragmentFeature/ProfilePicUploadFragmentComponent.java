package com.crux.qxm.di.profilePicUploadFragmentFeature;

import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.views.fragments.loginActivityFragments.ProfilePicUploadFragment;
import dagger.Component;

@ProfilePicUploadFragmentScope
@Component (modules = ProfilePicUploadFragmentModule.class, dependencies = AppComponent.class)
public interface ProfilePicUploadFragmentComponent {
    void injectProfilePicUploadFragment(ProfilePicUploadFragment profilePicUploadFragment);
}

