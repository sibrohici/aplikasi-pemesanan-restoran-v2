import java.util.Scanner;
public class Main {
   static Scanner input = new Scanner(System.in);
   
   static Menu[] daftarMenu = new Menu[50];
   static int jumlahMenu = 0;

   static final int PAJAK_PERSEN = 10;
   static final int BIAYA_LAYANAN = 20000;


static void simpanMenu(Menu m) {
    daftarMenu[jumlahMenu] = m;
    jumlahMenu++;
}

static void isiMenuAwal() {
    simpanMenu(new Menu("Nasi Padang", 25000,"Makanan"));
    simpanMenu(new Menu("Nasi Goreng", 20000,"Makanan"));
    simpanMenu(new Menu("Mie Jebew Pedas", 18000, "Makanan"));
    simpanMenu(new Menu("Mie Bangladesh", 18000, "Makanan"));
    simpanMenu(new Menu("Es Teh", 8000, "Minuman"));
    simpanMenu(new Menu("Es Jeruk", 8000, "Minuman"));
    simpanMenu(new Menu("Kopi Hitam", 10000, "Minuman"));
    simpanMenu(new Menu("Kopi Susu", 12000, "Minuman"));
}

static String rupiah(int angka) {
    return "Rp " + String.format("%,d", angka).replace(',', '.');
}

static void tampilkanDaftarMenu() {
    String[] semuaKategori = {"Makanan", "Minuman"};

    for (String kat : semuaKategori) {
        System.out.println("\n=== " + kat.toUpperCase() +" ===");
        for (int i = 0; i < jumlahMenu; i++) {
            if (daftarMenu[i].getKategori().equals(kat)) {
                System.out.printf("%2d. %-15s %s%n", i + 1,
                    daftarMenu[i].getNama(), rupiah(daftarMenu[i].getHarga()));
            }
        }
    }
}

static int bacaAngka(String pesan, int min, int max) {
    while (true) {
        System.out.print(pesan);
        String teks = input.nextLine().trim();
        try {
            int angka = Integer.parseInt(teks);
            if (angka >= min && angka <= max) {
                return angka;
            }
        
        } catch (NumberFormatException e) {

        }
        System.out.println("Input tidak valid, silakan coba lagi.");
    }
}

static boolean mintaKonfirmasi() {
    System.out.print("Ketik 'Ya' untuk melanjutkan (selain itu = batal): ");
    return input.nextLine().trim().equalsIgnoreCase("Ya");
}

static void prosesPesanan() {
    Menu[] pesananMenu = new Menu[daftarMenu.length];
    int[] pesananJumlah = new int[daftarMenu.length];
    int jumlahItem = 0;

    while (true) {
        tampilkanDaftarMenu();
        System.out.print("\nMasukkan nomor menu (ketik 'selesai' untuk mengakhiri): ");
        String teks = input.nextLine().trim();

        if (teks.equalsIgnoreCase("selesai")) {
            break;
        }

        int nomor;
        try {
            nomor = Integer.parseInt(teks);
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid. Masukkan nomor menu atau 'selesai'.");
            continue;
        }
        if (nomor < 1 || nomor > jumlahMenu) {
            System.out.println("Nomor menu tidak tersedia.");
            continue;
        }

        Menu dipilih = daftarMenu[nomor - 1];
        int jumlah = bacaAngka("Jumlah " + dipilih.getNama() + ": ", 1, 999);


        boolean sudahAda = false;
        for (int i = 0; i < jumlahItem; i++) {
            if (pesananMenu[i] == dipilih) {
                pesananJumlah[i] += jumlah;
                sudahAda = true;
                break;
            }
        }
        if (!sudahAda) {
            pesananMenu[jumlahItem] = dipilih;
            pesananJumlah[jumlahItem] = jumlah;
            jumlahItem++;
        }
        System.out.println(">> " + jumlah + " x " + dipilih.getNama() + " ditambahkan.");
    }

    if (jumlahItem == 0) {
        System.out.println("Belum ada pesanan.");
        return;
    }
    cetakStruk(pesananMenu, pesananJumlah, jumlahItem);
}

static int hitungSubtotal(Menu[] menu, int[] jumlah, int banyakItem) {
    int total = 0;
    for (int i = 0; i < banyakItem; i++) {
        total += menu[i].getHarga() * jumlah[i];
    }
    return total;
}


static int hitungPotonganMinuman(Menu[] menu, int[] jumlah, int banyakItem) {
    int potongan = 0;
    for (int i = 0; i < banyakItem; i++) {
        if (menu[i].getKategori().equals("Minuman")) {
            potongan += menu[i].getHarga() * (jumlah[i] / 2);
        }
    }
    return potongan;
}

static void cetakStruk(Menu[] menu, int[] jumlah, int banyakItem) {
    int subtotal = hitungSubtotal(menu, jumlah, banyakItem);

    int potongan = 0;
    int diskon = 0;
    if (subtotal > 50000) {
        potongan = hitungPotonganMinuman(menu, jumlah, banyakItem);
    }
    if (subtotal > 100000) {
        diskon = subtotal * 10 / 100;
    }
    int pajak = subtotal * PAJAK_PERSEN / 100;
    int totalBayar = subtotal - potongan - diskon + pajak + BIAYA_LAYANAN;

    System.out.println("\n=============== STRUK PESANAN ===============");
    System.out.printf("%-3s %-15s %4s %11s %11s%n", "No", "Item", "Qty", "Harga", "Total");
    for (int i = 0; i < banyakItem; i++) {
        int h = menu[i].getHarga();
        System.out.printf("%-3d %-15s %4s %11s %11s%n", i + 1, menu[i].getNama(),
                    jumlah[i], rupiah(h), rupiah(h * jumlah[i]));
    }
    System.out.println("---------------------------------------------");
    System.out.println("Total biaya pesanan      : " + rupiah(subtotal));
    
    if (potongan > 0) {
        System.out.println("Promo Beli 1 Gratis 1    : -" + rupiah(potongan));
    }
    if (diskon > 0) {
    System.out.println("Diskon 10%               : -" + rupiah(diskon));
    }
    if (potongan > 0 || diskon > 0) {
    System.out.println("Harga setelah diskon     : " + rupiah(subtotal - potongan - diskon));
    }
    System.out.println("Pajak " + PAJAK_PERSEN + "% (dari total)   : " + rupiah(pajak));
    System.out.println("Biaya pelayanan          : " + rupiah(BIAYA_LAYANAN));
    System.out.println("TOTAL BAYAR              : " + rupiah(totalBayar));
    System.out.println("=============================================");
}

    static void menuPemilik() {
        while (true) {
            System.out.println("\n--- KELOLA MENU ---");
            System.out.println("1. Tambah menu baru");
            System.out.println("2. Ubah harga menu");
            System.out.println("3. Hapus menu");
            System.out.println("0. Kembali");
            int pilihan = bacaAngka("Pilih: ", 0, 3);

            switch (pilihan) {
                case 1: tambahMenuBaru(); break;
                case 2: ubahHargaMenu(); break;
                case 3: hapusMenu(); break;
                case 0: return;
            }
        }
    }

    static void tambahMenuBaru() {
        int sisa = daftarMenu.length - jumlahMenu;
        if (sisa == 0) {
            System.out.println("Daftar menu sudah penuh.");
            return;
        }
        int banyak = bacaAngka("Berapa menu yang ingin ditambahkan (maks " + sisa + ")? ", 1, sisa);

        for (int i = 0; i < banyak; i++) {
            System.out.println("\nMenu baru ke-" + (i + 1));

            String nama;
            do {
                System.out.print("Nama menu: ");
                nama = input.nextLine().trim();
            } while (nama.isEmpty());

            int pilKategori = bacaAngka("Kategori (1=Makanan, 2=Minuman): ", 1, 2);
            String kategori;
            if (pilKategori == 1) {
                kategori = "Makanan";
            } else {
                kategori = "Minuman";
            }

            int harga = bacaAngka("Harga: Rp ", 1, 10000000);
            simpanMenu(new Menu(nama, harga, kategori));
        }
        System.out.println(">> Menu baru berhasil ditambahkan.");
    }

    static void ubahHargaMenu() {
        if (jumlahMenu == 0) {
            System.out.println("Belum ada menu.");
            return;
        }
        tampilkanDaftarMenu();
        int nomor = bacaAngka("\nNomor menu yang diubah: ", 1, jumlahMenu);
        Menu m = daftarMenu[nomor - 1];
        int hargaBaru = bacaAngka("Harga baru: Rp ", 1, 10000000);

        System.out.println("Ubah harga " + m.getNama() + " dari " + rupiah(m.getHarga())
                + " menjadi " + rupiah(hargaBaru) + "?");
        if (mintaKonfirmasi()) {
            m.setHarga(hargaBaru);
            System.out.println(">> Harga berhasil diubah.");
        } else {
            System.out.println(">> Perubahan dibatalkan.");
        }
    }

    static void hapusMenu() {
        if (jumlahMenu == 0) {
            System.out.println("Belum ada menu.");
            return;
        }
        tampilkanDaftarMenu();
        int nomor = bacaAngka("\nNomor menu yang dihapus: ", 1, jumlahMenu);
        Menu m = daftarMenu[nomor - 1];

        System.out.println("Hapus menu " + m.getNama() + "?");
        if (mintaKonfirmasi()) {
            for (int i = nomor - 1; i < jumlahMenu - 1; i++) {
                daftarMenu[i] = daftarMenu[i + 1];   // geser ke kiri
            }
            daftarMenu[jumlahMenu - 1] = null;
            jumlahMenu--;
            System.out.println(">> Menu berhasil dihapus.");
        } else {
            System.out.println(">> Penghapusan dibatalkan.");
        }
    }
        public static void main(String[] args) {
        isiMenuAwal();
        boolean jalan = true;

        while (jalan) {
            System.out.println("\n========== RESTORAN ==========");
            System.out.println("1. Pelanggan (pesan makanan/minuman)");
            System.out.println("2. Pemilik (kelola menu)");
            System.out.println("0. Keluar");
            int pilihan = bacaAngka("Pilih: ", 0, 2);

            switch (pilihan) {
                case 1: prosesPesanan(); break;
                case 2: menuPemilik(); break;
                case 0:
                    jalan = false;
                    System.out.println("Terima kasih!");
                    break;
            }
        }
    }
}