import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/service/authentication.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit, OnDestroy {
  form: FormGroup = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
    ]),
    firstName: new FormControl('', [Validators.required]),
    lastName: new FormControl('', [Validators.required]),
  });
  private error: string | null = null;
  private subscriptions: Subscription[] = [];

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {}

  ngOnInit(): void {}

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }

  submit() {
    if (this.form.valid) {
      this.subscriptions.push(
        this.authenticationService
          .register({
            username: this.form.get('username')?.value,
            password: this.form.get('password')?.value,
            firstName: this.form.get('firstName')?.value,
            lastName: this.form.get('lastName')?.value
          })
          .subscribe({
            next: (response: HttpResponse<any>) => {
              console.log(response);
              this.router.navigate(['/login']);
            },
            error: (err: HttpErrorResponse) => {
              this.error = err.error.message;
            },
          })
      );
    }
  }

  showError(): string | null {
    if (this.error) {
      return this.error;
    } else if (
      (!this.form.get('username')?.valid &&
        this.form.get('username')?.touched) ||
      (!this.form.get('password')?.valid && this.form.get('password')?.touched)
    ) {
      return 'Username or password invalid';
    }
    return null;
  }
}
