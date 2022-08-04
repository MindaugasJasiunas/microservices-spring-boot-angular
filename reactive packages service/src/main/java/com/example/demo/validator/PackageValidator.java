package com.example.demo.validator;

import com.example.demo.domain.Package;

import java.util.function.Function;

public interface PackageValidator extends Function<Package, PackageValidator.ValidationResult> {
    enum ValidationResult{
        SUCCESS,
        SENDER_DOESNT_EXIST, SENDER_ADDRESS_INVALID, SENDER_FIRSTNAME_INVALID, SENDER_LASTNAME_INVALID, SENDER_PHONE_NUMBER_INVALID, SENDER_COMPANY_INVALID,
        RECEIVER_DOESNT_EXIST, RECEIVER_ADDRESS_INVALID, RECEIVER_FIRSTNAME_INVALID, RECEIVER_LASTNAME_INVALID, RECEIVER_PHONE_NUMBER_INVALID, RECEIVER_COMPANY_INVALID,
        PACKAGE_DOESNT_EXIST, PACKAGES_WEIGHT_INVALID, NUM_OF_PACKAGES_INVALID, PACKAGE_DESCRIPTION_INVALID
    }

    static PackageValidator isPackageExists(){
        return pkg -> (pkg != null) ? ValidationResult.SUCCESS : ValidationResult.PACKAGE_DOESNT_EXIST;
    }

    static PackageValidator isSenderExists(){
        return pkg -> (pkg.getSender() != null) ? ValidationResult.SUCCESS : ValidationResult.SENDER_DOESNT_EXIST;
    }

    static PackageValidator isSenderAddressValid(){
        return pkg -> (pkg.getSender().getAddress() != null
                && pkg.getSender().getAddress().getAddress1() != null
                && !pkg.getSender().getAddress().getAddress1().trim().equals("")
                && pkg.getSender().getAddress().getHouseNumber() != null
                && !pkg.getSender().getAddress().getHouseNumber().trim().equals("")
                && pkg.getSender().getAddress().getCity() != null
                && !pkg.getSender().getAddress().getCity().trim().equals("")
                && pkg.getSender().getAddress().getState() != null
                && !pkg.getSender().getAddress().getState().trim().equals("")
                && pkg.getSender().getAddress().getPostalCode() != null
                && !pkg.getSender().getAddress().getPostalCode().trim().equals("")
                ) ? ValidationResult.SUCCESS : ValidationResult.SENDER_ADDRESS_INVALID;
    }

    static PackageValidator isSenderFirstNameValid(){
        return pkg -> (pkg.getSender().getFirstName() != null && !pkg.getSender().getFirstName().trim().equals("")) ? ValidationResult.SUCCESS : ValidationResult.SENDER_FIRSTNAME_INVALID;
    }

    static PackageValidator isSenderLastNameValid(){
        return pkg -> (pkg.getSender().getLastName() != null && !pkg.getSender().getLastName().trim().equals("")) ? ValidationResult.SUCCESS : ValidationResult.SENDER_LASTNAME_INVALID;
    }

    static PackageValidator isSenderPhoneNumberValid(){
        return pkg -> (pkg.getSender().getPhoneNumber() != null && !pkg.getSender().getPhoneNumber().trim().equals("")) ? ValidationResult.SUCCESS : ValidationResult.SENDER_PHONE_NUMBER_INVALID;
    }

    static PackageValidator isReceiverExists(){
        return pkg -> (pkg.getReceiver() != null) ? ValidationResult.SUCCESS : ValidationResult.RECEIVER_DOESNT_EXIST;
    }

    static PackageValidator isReceiverAddressValid(){
        return pkg -> (pkg.getReceiver().getAddress() != null
                && pkg.getReceiver().getAddress().getAddress1() != null
                && !pkg.getReceiver().getAddress().getAddress1().trim().equals("")
                && pkg.getReceiver().getAddress().getHouseNumber() != null
                && !pkg.getReceiver().getAddress().getHouseNumber().trim().equals("")
                && pkg.getReceiver().getAddress().getCity() != null
                && !pkg.getReceiver().getAddress().getCity().trim().equals("")
                && pkg.getReceiver().getAddress().getState() != null
                && !pkg.getReceiver().getAddress().getState().trim().equals("")
                && pkg.getReceiver().getAddress().getPostalCode() != null
                && !pkg.getReceiver().getAddress().getPostalCode().trim().equals("")
        ) ? ValidationResult.SUCCESS : ValidationResult.RECEIVER_ADDRESS_INVALID;
    }

    static PackageValidator isReceiverFirstNameValid(){
        return pkg -> (pkg.getReceiver().getFirstName() != null && !pkg.getReceiver().getFirstName().trim().equals("")) ? ValidationResult.SUCCESS : ValidationResult.RECEIVER_FIRSTNAME_INVALID;
    }

    static PackageValidator isReceiverLastNameValid(){
        return pkg -> (pkg.getReceiver().getLastName() != null && !pkg.getReceiver().getLastName().trim().equals("")) ? ValidationResult.SUCCESS : ValidationResult.RECEIVER_LASTNAME_INVALID;
    }

    static PackageValidator isReceiverPhoneNumberValid(){
        return pkg -> (pkg.getReceiver().getPhoneNumber() != null && !pkg.getReceiver().getPhoneNumber().trim().equals("")) ? ValidationResult.SUCCESS : ValidationResult.RECEIVER_PHONE_NUMBER_INVALID;
    }
    
    static PackageValidator isPackagesWeightValid(){
        return pkg -> (pkg.getPackageWeight() != null && pkg.getPackageWeight() > 0) ? ValidationResult.SUCCESS : ValidationResult.PACKAGES_WEIGHT_INVALID;
    }

    static PackageValidator isNumberOfPackagesValid(){
        return pkg -> (pkg.getNumberOfPackages() != null && pkg.getNumberOfPackages() > 0) ? ValidationResult.SUCCESS : ValidationResult.NUM_OF_PACKAGES_INVALID;
    }


    // OPTIONAL CHECKS
    static PackageValidator isPackageDescriptionValid(){
        return pkg -> (pkg.getPackageContentsDescription() != null && !pkg.getPackageContentsDescription().trim().equals("")) ? ValidationResult.SUCCESS : ValidationResult.PACKAGE_DESCRIPTION_INVALID;
    }
    static PackageValidator isSenderCompanyValid(){
        return pkg -> (pkg.getSender().getCompany() != null && !pkg.getSender().getCompany().trim().equals("")) ? ValidationResult.SUCCESS : ValidationResult.SENDER_COMPANY_INVALID;
    }
    static PackageValidator isReceiverCompanyValid(){
        return pkg -> (pkg.getReceiver().getCompany() != null && !pkg.getReceiver().getCompany().trim().equals("")) ? ValidationResult.SUCCESS : ValidationResult.RECEIVER_COMPANY_INVALID;
    }

    //at the end chain them together
    default PackageValidator and(PackageValidator other){
        return pkg -> {
            ValidationResult result = this.apply(pkg);
            // apply other validation or return error(result)
            return result.equals(ValidationResult.SUCCESS) ? other.apply(pkg) : result;
        };
    }
}
