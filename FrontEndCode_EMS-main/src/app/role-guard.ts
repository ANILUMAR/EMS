import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

/**
 * Second gate behind AuthGuard: it makes the three logins genuinely different.
 *
 * Routes declare who may enter via `data.roles`. A route without the key is open
 * to any signed-in role. When a role is refused it is sent to its own home page
 * instead of seeing a blank router outlet.
 */
@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(private router: Router, private snackBar: MatSnackBar) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const allowed = (route.data?.['roles'] as string[] | undefined) ?? [];
    const role = (localStorage.getItem('role') || '').trim().toUpperCase();

    if (allowed.length === 0 || allowed.includes(role)) {
      return true;
    }

    this.snackBar.open('Your role does not have access to that page.', 'Dismiss', {
      duration: 4000,
      horizontalPosition: 'right',
      verticalPosition: 'bottom'
    });
    this.router.navigate([RoleGuard.homeFor(role)]);
    return false;
  }

  /** Landing page per role, shared with the login redirect. */
  static homeFor(role: string): string {
    switch ((role || '').toUpperCase()) {
      case 'ADMIN':
        return '/dashboard';
      case 'TESTER':
        return '/tester-board';
      default:
        return '/developer-board';
    }
  }
}
