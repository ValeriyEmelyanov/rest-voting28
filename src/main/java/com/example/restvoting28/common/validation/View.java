package com.example.restvoting28.common.validation;

import jakarta.validation.groups.Default;

public class View {
    public interface OnCreate extends Default {}
    public interface OnUpdate extends Default {}

    public interface Admin extends Default {}
    public interface Profile extends Default {}

    public interface AdminOnCreate extends Admin, OnCreate {}
    public interface AdminOnUpdate extends Admin, OnUpdate {}
    public interface ProfileOnCreate extends Profile, OnCreate {}
    public interface ProfileOnUpdate extends Profile, OnUpdate {}
}
