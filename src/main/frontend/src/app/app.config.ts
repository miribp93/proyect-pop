import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { AuthInterceptor } from './services/auth-interceptor.service';
import { provideFirebaseApp, initializeApp } from '@angular/fire/app';
import { provideFirestore, getFirestore } from '@angular/fire/firestore';
import { provideAuth, getAuth } from '@angular/fire/auth';
import { provideDatabase, getDatabase } from '@angular/fire/database';  // Importar Database
import { environments } from '../environments/environments';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(),
    provideAnimationsAsync(),
    provideHttpClient(withInterceptors([AuthInterceptor])),

    // Inicializa Firebase
    provideFirebaseApp(() => initializeApp(environments.firebaseConfig)),

    // Proveer Firestore y Auth
    provideFirestore(() => getFirestore()),
    provideAuth(() => getAuth()),

    // Proveer Firebase Database
    provideDatabase(() => getDatabase())  // Proveer el servicio de Database
  ]
};
