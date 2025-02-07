import { inject, Injectable } from '@angular/core';
import { Database, ref, set, push, onValue } from '@angular/fire/database';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private db = inject(Database);
  private authService = inject(AuthService);

  // ✅ Enviar mensaje
  sendMessage(content: string, receiverId: string): Observable<void> {
    return new Observable<void>((subscriber) => {
      const currentUser = this.authService.getUsername(); // Obtener usuario autenticado

      if (!currentUser) {
        console.error('No authenticated user found!');
        subscriber.complete();
        return;
      }

      // 🔹 Crear un ID de chat único entre los dos usuarios
      const chatId = [currentUser, receiverId].sort().join('_');
      const newMessageRef = push(ref(this.db, `chats/${chatId}`));

      set(newMessageRef, {
        senderId: currentUser,
        receiverId,
        content,
        timestamp: new Date().toISOString()
      }).then(() => {
        console.log('Message sent!');
        subscriber.next();
        subscriber.complete();
      }).catch((error) => {
        console.error('Error sending message:', error);
        subscriber.error(error);
      });
    });
  }

  // ✅ Obtener mensajes con un usuario específico
  getMessagesWithUser(receiverId: string): Observable<any[]> {
    return new Observable((subscriber) => {
      const currentUser = this.authService.getUsername(); // Asegúrate de que esto obtiene el username correcto.

      if (!currentUser) {
        console.error('❌ No authenticated user found!');
        return;
      }

      // Asegurar que el chatId es siempre el mismo para ambas personas.
      const chatId =
        currentUser < receiverId
          ? `${currentUser}_${receiverId}`
          : `${receiverId}_${currentUser}`;

      console.log("🔍 Buscando mensajes para chatId:", chatId);

      const messagesRef = ref(this.db, `chats/${chatId}`);

      // Obtener mensajes de la base de datos de Firebase
      onValue(messagesRef, (snapshot) => {
        const messages = snapshot.val();
        console.log("📥 Mensajes obtenidos de Firebase:", messages);

        if (messages) {
          const messagesArray = Object.values(messages);
          console.log("📜 Mensajes en formato array:", messagesArray);
          subscriber.next(messagesArray);
        } else {
          console.log("⚠️ No hay mensajes en esta conversación.");
          subscriber.next([]);
        }
      });
    });
  }




  // ✅ Obtener todos los chats del usuario
  getUserChats(): Observable<any[]> {
    return new Observable((subscriber) => {
      const currentUser = this.authService.getUsername();
      console.log('🕵️ Usuario autenticado:', currentUser);

      if (!currentUser) return;

      const chatsRef = ref(this.db, `chats`);

      onValue(chatsRef, (snapshot) => {
        const allChats = snapshot.val();
        console.log('📩 Todos los chats:', allChats); // 🔍 Verifica estructura

        if (!allChats) {
          subscriber.next([]);
          return;
        }

        // 🔹 Extraer solo los chats donde el usuario participa
        const userChats = Object.keys(allChats)
          .filter((chatId) => chatId.includes(currentUser))
          .map((chatId) => {
            const messages = Object.values(allChats[chatId]); // 🔥 Extrae los mensajes correctamente
            return { chatId, messages };
          });

        console.log('📥 Chats del usuario:', userChats); // 🔍 Verifica resultado final
        subscriber.next(userChats);
      });
    });
  }


}
