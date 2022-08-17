import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Address } from 'src/app/model/address.model';
import { Package } from 'src/app/model/package.model';
import { Receiver } from 'src/app/model/receiver.model';
import { Sender } from 'src/app/model/sender.model';
import { ParcelService } from 'src/app/service/parcel.service';

@Component({
  selector: 'app-package-send',
  templateUrl: './package-send.component.html',
  styleUrls: ['./package-send.component.css'],
})
export class PackageSendComponent implements OnInit, OnDestroy {
  form: FormGroup = new FormGroup({
    packageContentsDesc: new FormControl(''),
    packageWeight: new FormControl('', [Validators.required]),
    packageQuantity: new FormControl('', [Validators.required]),
    fragile: new FormControl('', [Validators.required]),

    senderFirstName: new FormControl('', [Validators.required]),
    senderLastName: new FormControl('', [Validators.required]),
    senderPhoneNumber: new FormControl('', [Validators.required]),
    senderEmail: new FormControl('', [Validators.required]),
    senderAddress1: new FormControl('', [Validators.required]),
    senderAddress2: new FormControl(''),
    senderAddress3: new FormControl(''),
    senderHouseNumber: new FormControl('', [Validators.required]),
    senderApartmentNumber: new FormControl('', [Validators.required]),
    senderCity: new FormControl('', [Validators.required]),
    senderPostalCode: new FormControl('', [Validators.required]),
    senderState: new FormControl('', [Validators.required]),
    senderCompany: new FormControl(''),

    receiverFirstName: new FormControl('', [Validators.required]),
    receiverLastName: new FormControl('', [Validators.required]),
    receiverPhoneNumber: new FormControl('', [Validators.required]),
    receiverEmail: new FormControl('', [Validators.required]),
    receiverAddress1: new FormControl('', [Validators.required]),
    receiverAddress2: new FormControl(''),
    receiverAddress3: new FormControl(''),
    receiverHouseNumber: new FormControl('', [Validators.required]),
    receiverApartmentNumber: new FormControl('', [Validators.required]),
    receiverCity: new FormControl('', [Validators.required]),
    receiverPostalCode: new FormControl('', [Validators.required]),
    receiverState: new FormControl('', [Validators.required]),
    receiverCompany: new FormControl(''),
  });

  error: string | null = null;
  subscriptions: Subscription[] = [];

  constructor(private parcelService: ParcelService) {}

  ngOnInit(): void {}

  ngOnDestroy() {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }

  submit() {
    if (this.form.valid) {
      console.log(this.form.value);

      let senderAddress = new Address(
        this.form.get('senderAddress1')?.value,
        this.form.get('senderApartmentNumber')?.value,
        this.form.get('senderCity')?.value,
        this.form.get('senderHouseNumber')?.value,
        this.form.get('senderPostalCode')?.value,
        this.form.get('senderState')?.value,
        this.form.get('senderAddress2')?.value,
        this.form.get('senderAddress3')?.value
      )

      let receiverAddress = new Address(
        this.form.get('receiverAddress1')?.value,
        this.form.get('receiverApartmentNumber')?.value,
        this.form.get('receiverCity')?.value,
        this.form.get('receiverHouseNumber')?.value,
        this.form.get('receiverPostalCode')?.value,
        this.form.get('receiverState')?.value,
        this.form.get('receiverAddress2')?.value,
        this.form.get('receiverAddress3')?.value
      );

      let sender = new Sender(
        this.form.get('senderFirstName')?.value,
        this.form.get('senderLastName')?.value,
        this.form.get('senderPhoneNumber')?.value,
        this.form.get('senderEmail')?.value,
        senderAddress,
        this.form.get('senderCompany')?.value
      );

      let receiver = new Receiver(
        this.form.get('receiverFirstName')?.value,
        this.form.get('receiverLastName')?.value,
        this.form.get('receiverPhoneNumber')?.value,
        this.form.get('receiverEmail')?.value,
        receiverAddress,
        this.form.get('receiverCompany')?.value
      )


      let parcel = new Package(
        '00000000-0000-0000-0000-000000000000',  // doesn't matter - auto-generated at backend
        '00000000000000000000000000000000',  // doesn't matter - auto-generated at backend
        this.form.get('packageContentsDesc')?.value,
        'NEW',
        this.form.get('packageWeight')?.value,
        this.form.get('fragile')?.value,
        false,
        this.form.get('packageQuantity')?.value,
        sender,
        receiver
      );

      this.subscriptions.push(
        this.parcelService.createNewParcel(parcel).subscribe({
            next: (data: HttpResponse<any>)=>{
              console.log(data);
              console.log(data.status); // 200
              console.log(data.statusText); // 'OK'
            },
            error: (err: HttpErrorResponse)=> {
              this.error = err.error.message;
            }
          })
      );
    }else{
      this.error = 'Form contains errors.';
    }
  }

  showError(): string | null {
    if (this.error) {
      return this.error;
    } /*else if (
      (!this.form.get('username')?.valid &&
        this.form.get('username')?.touched) ||
      (!this.form.get('password')?.valid && this.form.get('password')?.touched)
    ) {
      return 'Username or password invalid';
    }*/
    return null;
  }
}
