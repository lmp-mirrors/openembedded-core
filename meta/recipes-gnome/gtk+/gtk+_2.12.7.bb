require gtk+.inc

PR = "r13"

SRC_URI = "http://download.gnome.org/sources/gtk+/2.12/gtk+-${PV}.tar.bz2 \
           file://xsettings.patch \
           file://run-iconcache.patch \
           file://disable-print.patch \
           file://hardcoded_libtool.patch \
           file://no-demos.patch \
           file://cellrenderer-cairo.patch;striplevel=0 \
           file://entry-cairo.patch;striplevel=0 \
           file://toggle-font.diff;striplevel=0 \
           file://scrolled-placement.patch;striplevel=0 \
           file://filesystem-volumes.patch \
           file://filechooser-props.patch \
           file://filechooser-default.patch \
           file://filechooser-sizefix.patch \
	  "
# temporary
#           file://gtklabel-resize-patch
#           file://menu-deactivate.patch
#        file://combo-arrow-size.patch;striplevel=0
# die die die
#           file://pangoxft2.10.6.diff


EXTRA_OECONF = "--without-libtiff --disable-xkb --disable-glibtest --enable-display-migration"

LIBV = "2.10.0"

PACKAGES_DYNAMIC += "gdk-pixbuf-loader-* gtk-immodule-* gtk-printbackend-*"

python populate_packages_prepend () {
	import os.path

	prologue = bb.data.getVar("postinst_prologue", d, 1)
	postinst_pixbufloader = bb.data.getVar("postinst_pixbufloader", d, 1)

	gtk_libdir = bb.data.expand('${libdir}/gtk-2.0/${LIBV}', d)
	loaders_root = os.path.join(gtk_libdir, 'loaders')
	immodules_root = os.path.join(gtk_libdir, 'immodules')
	printmodules_root = os.path.join(gtk_libdir, 'printbackends');

	do_split_packages(d, loaders_root, '^libpixbufloader-(.*)\.so$', 'gdk-pixbuf-loader-%s', 'GDK pixbuf loader for %s', postinst_pixbufloader)
	do_split_packages(d, immodules_root, '^im-(.*)\.so$', 'gtk-immodule-%s', 'GTK input module for %s', prologue + 'gtk-query-immodules-2.0 > /etc/gtk-2.0/gtk.immodules')
	do_split_packages(d, printmodules_root, '^libprintbackend-(.*)\.so$', 'gtk-printbackend-%s', 'GTK printbackend module for %s')

        if (bb.data.getVar('DEBIAN_NAMES', d, 1)):
                bb.data.setVar('PKG_${PN}', '${MLPREFIX}libgtk-2.0', d)
}
