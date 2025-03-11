package com.example.TP.Tests.unitaires;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CompteBancaireTest {
    private CompteBancaire compte;
    private NotificationService notificationServiceMock;

    @BeforeEach
    public void setUp() {

        notificationServiceMock = mock(NotificationService.class);

        compte = new CompteBancaire(1000, notificationServiceMock);
    }


    @Test
    public void testDeposer() {
        doNothing().when(notificationServiceMock).envoyerNotification(anyString());

        compte.deposer(500);
        verify(notificationServiceMock, times(1)).envoyerNotification("Dépôt de 500.0 effectué.");

        assertEquals(1500, compte.getSolde());
    }
    @Test
    void testRetraitSuperieurSolde() {
        NotificationService notificationServiceMock = mock(NotificationService.class);
        CompteBancaire compte = new CompteBancaire(100, notificationServiceMock);

        assertThrows(IllegalArgumentException.class, () -> {
            compte.retirer(150);
        });
        verify(notificationServiceMock, never()).envoyerNotification(anyString());
    }
    @Test
    public void testDepotsSuccessifs() {
        doNothing().when(notificationServiceMock).envoyerNotification(anyString());
        compte.deposer(500);
        compte.deposer(300);
        verify(notificationServiceMock, times(2)).envoyerNotification(anyString());
    }
    @Test
    public void testTransfererVers() {
        NotificationService notificationServiceMock2 = mock(NotificationService.class);
        CompteBancaire autreCompte = new CompteBancaire(500, notificationServiceMock2);

        doNothing().when(notificationServiceMock).envoyerNotification(anyString());
        doNothing().when(notificationServiceMock2).envoyerNotification(anyString());
        compte.transfererVers(autreCompte, 300);

        verify(notificationServiceMock, times(1)).envoyerNotification("Retrait de 300.0 effectué.");
        verify(notificationServiceMock2, times(1)).envoyerNotification("Dépôt de 300.0 effectué.");

        assertEquals(700, compte.getSolde());
        assertEquals(800, autreCompte.getSolde());
    }

}